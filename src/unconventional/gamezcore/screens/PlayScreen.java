package unconventional.gamezcore.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import unconventional.gamezcore.Pools.WallPool;
import unconventional.gamezcore.game.GameApp;
import unconventional.gamezcore.handlers.PlayContactListener;
import unconventional.gamezcore.handlers.Res;
import unconventional.gamezcore.objects.Ball;
import unconventional.gamezcore.objects.Wall;
import unconventional.gamezcore.Hud.PlayHud;
import unconventional.gamezcore.handlers.Cons;

/**
 * Created by Rafael on 10/3/2016.
 *
 */
public class PlayScreen implements Screen {

    // debugging
    private Box2DDebugRenderer b2dr;
    private OrthographicCamera b2dcam;

    // private
    private boolean centerOnPlayer = true; // for ortho cams in handlerCamera()
    private SpriteBatch sb;
    private World world;

    // Array objects
    private Array<unconventional.gamezcore.objects.Plat> platforms;
    private Array<Wall> walls;
    private Array<unconventional.gamezcore.pickups.ScorePickup> scorePickups;
    private Array<unconventional.gamezcore.particleeffects.BarEffect> barEffects;
    private Array<unconventional.gamezcore.particleeffects.BallExplosion> ballExplosions;

    private Array<unconventional.gamezcore.objects.Plat> lvlPlats;

    // Pools
    private unconventional.gamezcore.Pools.PlatPool platPool;
    private WallPool wallPool;
    private unconventional.gamezcore.Pools.ScorePickupPool spPool;
    private unconventional.gamezcore.Pools.BarEffectPool barEffectPool;
    private unconventional.gamezcore.Pools.BallExplosionPool ballExplosionPool;

    // For Hud
    private PlayHud hud;
    private int currentDepth = -1;
    public int currentScore = 0;

    // contact listener
    private PlayContactListener cl;

    // player
    private Ball player;
    
    private int playSpeed = 1;
    private boolean isPlaySpeedUpdated = false;

    // Is this disposed
    private boolean isDisposed = false;

    // Score stage
    private ScoreStage scoreStage;

    // pseudo Screen logic
    private boolean isPlayingScreen = true;


    // debuging
    private int pickupsCreated;

    public PlayScreen() {
        world = new World(new Vector2(0, 0), true);
        this.sb = GameApp.APP.getSb();


        cl = new PlayContactListener(this);
        world.setContactListener(cl);

        // Box2d Cam
        b2dr = new Box2DDebugRenderer();
        b2dcam = new OrthographicCamera();
        b2dcam.setToOrtho(false, Gdx.graphics.getWidth() * Cons.SCALE / Cons.PPM, Gdx.graphics.getHeight() * Cons.SCALE / Cons.PPM);


        // arrays
        platforms = new Array<unconventional.gamezcore.objects.Plat>();
        walls = new Array<Wall>();
        scorePickups = new Array<unconventional.gamezcore.pickups.ScorePickup>();
        barEffects = new Array<unconventional.gamezcore.particleeffects.BarEffect>();
        ballExplosions = new Array<unconventional.gamezcore.particleeffects.BallExplosion>();

        lvlPlats = new Array<unconventional.gamezcore.objects.Plat>();

        // Pools
        platPool = new unconventional.gamezcore.Pools.PlatPool(world);
        wallPool = new WallPool(world);
        spPool = new unconventional.gamezcore.Pools.ScorePickupPool(world);
        barEffectPool = new unconventional.gamezcore.Pools.BarEffectPool(this);
        ballExplosionPool = new unconventional.gamezcore.Pools.BallExplosionPool();

        // Map
        initializeMap();

        // spawn location should be fully based on Vir values
        player = new Ball(world, this, new Vector2(Cons.VIR_WIDTH / 2, -Cons.VIR_HEIGHT / 2 - Cons.BALL_DIAM));
        hud = new PlayHud(this);

        scoreStage = new ScoreStage(this);
    }

    // called everytime the game sets the screen to this class
    public void show() {

    }

    /**
     * in initialize map we create the side walls at height currentDepth = 0 (VIR_HEIGHT * currentDepth)
     * and the top plat
     */
    private void initializeMap() {

        // create the first 2 walls by default
        Wall leftSide = wallPool.obtain();
        leftSide.setBodyPosition(Cons.LEFT_WALL_X, 0f);

        Wall rightSide = wallPool.obtain();
        rightSide.setBodyPosition(Cons.RIGHT_WALL_X, 0);

        walls.add(leftSide);
        walls.add(rightSide);
    }


    /*
    Given the depth currentDepth,
    in initialize map we create the side walls at height currentDepth = 0 (VIR_HEIGHT * currentDepth)
    then at every even number of currentDepth (currentDepth starts at -1) this method draws
    both walls and horizonal plats at height (VIR_HEIGHT * (currentDepth+1)) and (VIR_HEIGHT * (currentDepth+2))
    */
    boolean currentNgenerated = false; // have (currentDepth+1) and (currentDepth+2) plats been generated?

    private void autoGenerateMap() {
        //System.out.println("Map-Step,  num pickups: " + scorePickups.size);
        currentDepth = Math.abs((int) (player.getVirY() / Cons.WALL_HEIGHT));

        if (currentDepth % 2 == 1)
            currentNgenerated = false;

        if (currentDepth % 2 == 0 && !currentNgenerated) {
            currentNgenerated = true;
            generateWalls(); // draw currentDepth+1, and currentDepth+2 depth WALLS
            generatePlats2();
        }
    }

    private unconventional.gamezcore.objects.Plat prevPlat = null; // necessary to build a ScorePickup between every platform, (instance variable scope is necessary to link the 8th and the 9th)

    private unconventional.gamezcore.objects.Plat prevLeftPlat = null;
    private unconventional.gamezcore.objects.Plat prevRightPlat = null; // btw might this cause problems for the pool
    /**
     * generates the n-1, n, n+1 walls (n is currentDepth)
     * so basically generates 8 plats at a time
     */
    private void generatePlats() {
        boolean rightAngularVelocity = false;

        // draw currentDepth+1, and currentDepth+2 depth PLATFORMS
        for (int i = 1; i < 3; i++) { // 3 should always stay as a 3
            // 4 plats per depth
            for (int j = 0; j < Cons.PLATS_PER_DEPTH; j++) {
                // (int)(BALL_DIAM*1.5) to leave 1.5 of diam as minimum space for ball to pass thru
                float r = MathUtils.random((float) (Cons.BALL_DIAM * 1.5), Cons.VIR_WIDTH - Cons.PLAT_WIDTH - (float) (Cons.BALL_DIAM * 1.5));

                // Get CURRENT PLAT
                unconventional.gamezcore.objects.Plat plat = platPool.obtain();
                plat.setBodyPosition(r, (-(currentDepth + i) * Cons.VIR_HEIGHT - j * (Cons.VIR_HEIGHT / Cons.PLATS_PER_DEPTH)));
                //                   r,   -(overall depth, top of screen) - ( depth between each plat)

                // update angular velocity, when player depth (currentDepth) is > 1, start adding angular velocity
                if (currentDepth > 1 && j % 2 == 0) {
                    // Handle Angular Velocity
                    float rAngle = MathUtils.random(1f, 2f);
                    rightAngularVelocity = !rightAngularVelocity;
                    if (rightAngularVelocity)
                        plat.setAngularVel(rAngle);
                    else
                        plat.setAngularVel(-rAngle);
                }
                platforms.add(plat);


                // Generate Pickups
                if (prevPlat != null) {
                    float spX = (prevPlat.getX() + plat.getX()) / 2 + Cons.PLAT_WIDTH / 2;
                    float spY = (prevPlat.getY() + plat.getY()) / 2 + Cons.PLAT_HEIGHT / 2;
                    unconventional.gamezcore.pickups.ScorePickup sp = spPool.obtain();
                    sp.setBodyPosition(spX, spY);
                    scorePickups.add(sp);
                }
                prevPlat = plat;
            }

        }
    }

    public void generatePlats2() {
        // draw currentDepth+1, and currentDepth+2 depth PLATFORMS
        for (int i = 1; i < 3; i++) { // 3 should always stay as a 3
            // 4 plats per depth
            for (int j = 0; j < Cons.PLATS_PER_DEPTH; j++) {
                float y = (-(currentDepth + i) * Cons.VIR_HEIGHT - j * (Cons.VIR_HEIGHT / Cons.PLATS_PER_DEPTH));

                // decreasing gap widths
                float gapWidth = (float) Cons.VIR_WIDTH / 2 - playSpeed * Cons.VIR_WIDTH * 0.04f;
                gapWidth = Math.max(gapWidth, (float) (Cons.BALL_DIAM * 2));


                float leftWidth = MathUtils.random(Cons.VIR_WIDTH * 0.05f,  Cons.VIR_WIDTH - gapWidth - Cons.VIR_WIDTH * 0.05f);
                // Get CURRENT PLAT
                unconventional.gamezcore.objects.Plat leftPlat = platPool.obtain();
                leftPlat.setSize(leftWidth, Cons.PLAT_HEIGHT);
                leftPlat.setBodyPosition(Cons.WALL_WIDTH, y);

                //                   r,   -(overall depth, top of screen) - ( depth between each plat)

                unconventional.gamezcore.objects.Plat rightPlat = platPool.obtain();
                rightPlat.setSize(Cons.VIR_WIDTH - leftWidth - gapWidth, Cons.PLAT_HEIGHT);
                rightPlat.setBodyPosition(leftPlat.getWidth() + gapWidth, y);

                platforms.add(leftPlat);
                platforms.add(rightPlat);



                if (prevLeftPlat != null && prevRightPlat != null) {
                    float spX = (prevLeftPlat.getX() + rightPlat.getX()) / 2 + Cons.PLAT_WIDTH / 2;
                    float spY = (prevLeftPlat.getY() + rightPlat.getY()) / 2 + Cons.PLAT_HEIGHT / 2;
                    unconventional.gamezcore.pickups.ScorePickup sp = new unconventional.gamezcore.pickups.ScorePickup(world, spX, spY);
                    scorePickups.add(sp);
                    pickupsCreated++;
                }
                prevLeftPlat = leftPlat;
                prevRightPlat = rightPlat;
            }

        }

        //System.out.println("numpikcups in array: " + scorePickups.size + "   total pickups created: " + pickupsCreated);
    }

    /**
     * generates the n-1, n, n+1 walls (n is currentDepth)
     */
    private void generateWalls() {
        for (int i = 1; i < 3; i++) {
            Wall leftSide = wallPool.obtain();
            leftSide.setBodyPosition(Cons.LEFT_WALL_X, -(currentDepth + i) * Cons.WALL_HEIGHT);

            Wall rightSide = wallPool.obtain();
            rightSide.setBodyPosition(Cons.RIGHT_WALL_X, -(currentDepth + i) * Cons.WALL_HEIGHT);

            walls.add(leftSide);
            walls.add(rightSide);
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        handleBackInput();
        if (isDisposed) // must come right after handleBackInput()
            return;

        updatePlaySpeed();


        if (player.isCrashed() && !player.isBodyDestroyed()) {
            Gdx.input.setInputProcessor(scoreStage.getStage());
            updateBestScore(currentScore);

            isPlayingScreen = false;
            if (!player.isBodyDestroyed()) {
                player.destroy();
            }
        }

        //b2dr.render(world, b2dcam.combined);

        if (isPlayingScreen) {
            // updating
            autoGenerateMap();

            updateCameras(!centerOnPlayer); // game and b2d cams

            // rendering
            sb.begin();

            sb.setProjectionMatrix(GameApp.APP.getFontcam().combined);
            renderAndRemoveBarEffects();

            // GAME CAM
            sb.setProjectionMatrix(GameApp.APP.getCam().combined);

            renderAndRemovePlats();
            renderAndRemoveWalls();
            renderAndFreeScorePickups();
            player.render();


            sb.setProjectionMatrix(GameApp.APP.getFontcam().combined);
            hud.render();
            sb.end();


        } else { // else scoreStage
            scoreStage.getStage().act();
            sb.begin();
            // GAME CAM
            sb.setProjectionMatrix(GameApp.APP.getCam().combined);
            renderAndRemovePlats();
            renderAndRemoveWalls();
            renderAndFreeScorePickups();
            for (unconventional.gamezcore.particleeffects.BallExplosion b : ballExplosions)
                b.render();

            // Hud cam
            sb.setProjectionMatrix(GameApp.APP.getFontcam().combined);
            scoreStage.render(); // render here
            //hud.render();
            hud.getRain().render();

            scoreStage.getStage().draw();

            sb.end();
        }


        //System.out.println("num pickups: " + scorePickups.size + "  num plats " + platforms.size);
        world.step(Gdx.graphics.getDeltaTime(), 6, 2);
    }

    private void updatePlaySpeed() {
        if (getScore() % 3 == 0 && getScore() != 0){
            if (!isPlaySpeedUpdated){
                playSpeed++;
                isPlaySpeedUpdated = true;

                // update Rain's count, bg's count, ball's count, .., color // make an animation with the score font to clearly indicate you reached a new "playSpeed"
                // so have all of the things named above have a starting particleCount, a max particlecount, ect..
                // and they will all have a method updateEffect()
                hud.getRain().setMaxEffectParticles(playSpeed);
                player.setTailLife(playSpeed);
            }
        } else{
            isPlaySpeedUpdated = false;
        }
    }


    private void updateBestScore(int currentScore) {
        int bestScore = Res.prefs.getInteger("bestscore");
        if (currentScore > bestScore) {
            Res.prefs.putInteger("bestscore", currentScore);
            Res.prefs.flush();
            GameApp.APP.getPlayServices().submitScore(currentScore);
        }
    }

    /**
     * Puts Walls and their 2d bodies that were left behind back into the wallPool.
     */
    private void renderAndRemoveWalls() {
        for (Wall w : walls) {
            w.render();
            if (currentDepth > 2) {
                if (w.getY() > -(currentDepth - 2) * Cons.VIR_HEIGHT) {
                    walls.removeValue(w, true); // true is to use .equals()
                    wallPool.free(w); // add back to pool
                }
            }
        }
    }

    /**
     * Puts Plats and their 2d bodies that were left behind back into the platPool.
     */
    private void renderAndRemovePlats() {

        for (unconventional.gamezcore.objects.Plat p : platforms) {
            p.render();
                if (p.getY() > player.getVirY() + Cons.VIR_HEIGHT / 2) {
                    platforms.removeValue(p, true); // true is to use .equals()
                    platPool.free(p); // add back to pool
                }
        }
    }

    /**
     * render ScorePickups and free them if player is already past them
     */
    private void renderAndFreeScorePickups() {

        // remove ones signaled by PlayContactListener
        for (unconventional.gamezcore.pickups.ScorePickup sp : spToRemove){
            scorePickups.removeValue(sp, true); // true is to use .equals()
            spToRemove.removeValue(sp, true);
        }


        for (unconventional.gamezcore.pickups.ScorePickup sp : scorePickups) {
                if (sp.getY() > player.getVirY() + Cons.VIR_HEIGHT / 2) {
                    scorePickups.removeValue(sp, true); // true is to use .equals()

                }else{
                    sp.render();
                }
        }


    }

    private Array<unconventional.gamezcore.pickups.ScorePickup> spToRemove = new Array<unconventional.gamezcore.pickups.ScorePickup>();

    public void setToRemoveScorePickup(unconventional.gamezcore.pickups.ScorePickup sp){
        spToRemove.add(sp);
    }

    private void renderAndRemoveBarEffects() {
        for (unconventional.gamezcore.particleeffects.BarEffect be : barEffects) {
            if (be.isComplete()) {
                barEffects.removeValue(be, true);
                barEffectPool.free(be);
            }
            be.render();
        }
    }

    // update gamecam and b2dcam
    private void updateCameras(boolean centerOnPlayer) {
        if (centerOnPlayer) {
            GameApp.APP.getCam().position.set(
                    player.getVirX(),
                    player.getVirY() - (Gdx.graphics.getHeight() / 3),
                    0);
            GameApp.APP.getCam().update();

            b2dcam.position.set(player.getVirX() / Cons.PPM, player.getVirY() / Cons.PPM - (Gdx.graphics.getHeight() / 3) / Cons.PPM, 0);
            b2dcam.update();

        } else {
            // BALL_POS_ON_HUD = player.getVirY() - VIR_HEIGHT * 0.20f
            GameApp.APP.getCam().position.set(Cons.VIR_WIDTH / 2, player.getVirY() - Cons.VIR_HEIGHT * 0.20f, 0);
            GameApp.APP.getCam().update();

            b2dcam.position.set(Cons.VIR_WIDTH / 2 / Cons.PPM, player.getVirY() / Cons.PPM - Cons.VIR_HEIGHT * 0.2f / Cons.PPM, 0);
            b2dcam.update();

        }


    }

    /**
     * GOTTA IMPLEMENT THIS IN A DIFF WAY CAUSE U GOTTA MAKE SURE YOU STOP THE CURRENT RENDER
     * LOOP WITH A RETURN, SO THAT NOTHING ELSE IS CALLED ONCE EVERYTHING IS DISPOSED
     */
    private void handleBackInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.BACK)) {
            if (!isPlayingScreen) {
                GameApp.APP.isBackPressed = true;
                dispose();
                isDisposed = true;
                GameApp.APP.setScreen(new MainScreen());

            }


        }
    }

    // GETTERS

    public int getScore() {
        return currentScore;
    }

    public Ball getPlayer() {
        return player;
    }

    public unconventional.gamezcore.Pools.ScorePickupPool getSpPool() {
        return spPool;
    }

    public Array<unconventional.gamezcore.pickups.ScorePickup> getScorePickups() {
        return scorePickups;
    }

    public int getCurrentDepth() {
        return currentDepth;
    }

    // SETTERS

    public void setBarEffect(float virX, float virY) {
        unconventional.gamezcore.particleeffects.BarEffect be = barEffectPool.obtain();
        be.setPosition(virX, virY);
        be.setCurrentParticleCount(playSpeed);
        barEffects.add(be);
    }

    public void renderBallExplosion(float virX, float virY) {
        unconventional.gamezcore.particleeffects.BallExplosion be = ballExplosionPool.obtain();
        be.setPosition(virX, virY);
        ballExplosions.add(be);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        if (player != null)
            player.dispose();
        world.dispose();
        hud.dispose();
        for (unconventional.gamezcore.pickups.ScorePickup sp : scorePickups)
            sp.dispose();
        for (unconventional.gamezcore.particleeffects.BarEffect be : barEffects)
            be.dispose();
        for (unconventional.gamezcore.particleeffects.BallExplosion be : ballExplosions)
            be.dispose();
    }

    public float getPlaySpeed() {
        return playSpeed;
    }
}