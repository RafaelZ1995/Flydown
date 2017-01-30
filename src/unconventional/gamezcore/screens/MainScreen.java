package unconventional.gamezcore.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;

import unconventional.gamezcore.Hud.Rain;
import unconventional.gamezcore.Pools.WallPool;
import unconventional.gamezcore.game.GameApp;
import unconventional.gamezcore.handlers.MainScreenCL;
import unconventional.gamezcore.handlers.Res;
import unconventional.gamezcore.objects.Wall;
import unconventional.gamezcore.handlers.Cons;

/**
 * Created by Rafael on 10/3/2016.
 *
 */
public class MainScreen implements Screen {

    // debugging
    private Box2DDebugRenderer b2dr;
    private OrthographicCamera b2dcam;
    private boolean centerOnPlayer = true; // for ortho cams in handlerCamera()
    //private GameApp game;
    private SpriteBatch sb;
    private World world;

    // Array objects
    private Array<Wall> walls;

    // Pools
    private WallPool wallPool;

    // DEPTH
    private int currentDepth = -1; // current depth (how many VIR_HEIGHT's the player has gone down)

    // This screen
    private boolean isDisposed = false;

    // player
    private unconventional.gamezcore.objects.MainScreenBall player;

    // UI
    private Stage stage;
    Button playButton;
    private boolean isBackStillPressed = false;
    private Rain rain;
    private unconventional.gamezcore.Hud.MainScreenArrowCharger arrowCharger;

    // Title
    private PermanentText titlePText = new PermanentText("Infinite Flydown", Cons.VIR_WIDTH * 0.5f, Cons.VIR_HEIGHT * 0.9f, "128");

    // lets use some Math to animate sizes!
    private double dt;
    private float playButtonExtraSize = 0;

    private Tutorial tut;
    private boolean renderInfo;
    private unconventional.gamezcore.screens.InfoBlock infoBlock = new unconventional.gamezcore.screens.InfoBlock();;

    public MainScreen() {
        world = new World(new Vector2(0, -2.81f * 0), true);
        this.sb = GameApp.APP.getSb();


        world.setContactListener(new MainScreenCL());

        // Box2d Cam
        b2dr = new Box2DDebugRenderer();
        b2dcam = new OrthographicCamera();
        b2dcam.setToOrtho(false, Gdx.graphics.getWidth() * Cons.SCALE / Cons.PPM, Gdx.graphics.getHeight() * Cons.SCALE / Cons.PPM);


        // Walls
        walls = new Array<Wall>();
        wallPool = new WallPool(world);

        // Map
        initializeMap();

        // spawn location should be fully based on Vir values
        player = new unconventional.gamezcore.objects.MainScreenBall(world, new Vector2(Cons.VIR_WIDTH / 2, -Cons.VIR_HEIGHT / 2 - Cons.BALL_DIAM));

        // UI
        rain = new Rain();
        rain.forceMaxEffectParticles(20);
        arrowCharger = new unconventional.gamezcore.Hud.MainScreenArrowCharger(player);
        stage = new Stage();

        // play button
        Drawable drawable1 = new TextureRegionDrawable(new TextureRegion(Res.playButtonRegion));
        playButton = new Button(drawable1);
        playButton.setWidth(Cons.VIR_WIDTH / 3);
        playButton.setHeight(Cons.VIR_HEIGHT / 5); // set in terms of texture.width? (png file)
        playButton.setOrigin(Cons.VIR_WIDTH / 3 / 2, Cons.VIR_HEIGHT / 5 / 2);

        playButton.setX(Cons.VIR_WIDTH / 2 - playButton.getWidth() / 2);
        playButton.setY(Cons.VIR_HEIGHT / 3 - playButton.getHeight() / 2);
        playButton.setColor(GameApp.CurrentThemeColor);
        stage.addActor(playButton);

        // info button
        Drawable drawable2 = new TextureRegionDrawable(new TextureRegion(Res.infoPlusRegion));
        Button infoButton = new Button(drawable2);
        infoButton.setSize(Cons.VIR_WIDTH * 0.15f, Cons.VIR_WIDTH * 0.15f);
        infoButton.setOrigin(Cons.VIR_WIDTH * 0.1f / 2, Cons.VIR_WIDTH * 0.1f / 2);

        infoButton.setPosition(Cons.VIR_WIDTH * 0.5f - infoButton.getWidth() / 2, Cons.VIR_HEIGHT * 0.1f);
        infoButton.setColor(GameApp.CurrentThemeColor);
        stage.addActor(infoButton);

        // leaderboard button
        Drawable drawable3 = new TextureRegionDrawable(new TextureRegion(Res.leaderboardRegion));
        Button leaderboardButton = new Button(drawable3);
        leaderboardButton.setSize(Cons.VIR_WIDTH * 0.15f, Cons.VIR_WIDTH * 0.15f);
        leaderboardButton.setOrigin(Cons.VIR_WIDTH * 0.1f / 2, Cons.VIR_WIDTH * 0.1f / 2);

        leaderboardButton.setPosition(Cons.VIR_WIDTH * 0.25f - leaderboardButton.getWidth() / 2, Cons.VIR_HEIGHT * 0.4f);
        leaderboardButton.setColor(GameApp.CurrentThemeColor);
        stage.addActor(leaderboardButton);


        // info button listener
        infoButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                renderInfo = !renderInfo;
                return true;
            }
        });

        // leaderboard button listener
        leaderboardButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                GameApp.APP.getPlayServices().showScore(); // open leaderboards
                return true;
            }
        });

        // set both player and stage as input processors
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(player);
        inputMultiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(inputMultiplexer);

        playButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //System.out.println("Play button from Pseudo Main screen");
                unconventional.gamezcore.screens.PlayScreen ps = new unconventional.gamezcore.screens.PlayScreen();
                dispose();
                GameApp.APP.setScreen(ps);
                return true;
            }
        });


        tut = new Tutorial();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        handleBackInput();
        if (isDisposed)
            return;

        stage.act();
        stage.draw();
        // update
        update();
        updateCameras(!centerOnPlayer); // game and b2d cams
        autoGenerateMap();

        sb.begin();
        // GAME CAM
        sb.setProjectionMatrix(GameApp.APP.getCam().combined);
        renderAndRemoveWalls();
        player.render();

        // HUD CAM
        sb.setProjectionMatrix(GameApp.APP.getFontcam().combined);

        tut.render();
        rain.render(); // must put before stage.draw() apparently
        //Res.font128.getData().setScale(1f,2f);
        titlePText.render();
        //Res.font128.getData().setScale(1f, 1f);
        arrowCharger.render();
        if (renderInfo)
            infoBlock.render();
        sb.end();



        world.step(Gdx.graphics.getDeltaTime(), 6, 2);
    }

    private void update() {
        dt += 0.02f;
        playButtonExtraSize = (float) Math.sin(dt) * 70;
        playButton.setWidth(Cons.VIR_WIDTH / 3 + playButtonExtraSize);
        playButton.setHeight(Cons.VIR_HEIGHT / 5 + playButtonExtraSize);
        playButton.setX(Cons.VIR_WIDTH / 2 - playButton.getWidth() / 2 );
        playButton.setY(Cons.VIR_HEIGHT / 3 - playButton.getHeight() / 2 );
        playButton.setColor(GameApp.CurrentThemeColor);
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
        currentDepth = Math.abs((int) (player.getVirY() / Cons.WALL_HEIGHT));

        if (currentDepth % 2 == 1)
            currentNgenerated = false;

        if (currentDepth % 2 == 0 && !currentNgenerated) {
            currentNgenerated = true;
            generateWalls(); // draw currentDepth+1, and currentDepth+2 depth WALLS
            //generateScorePickups();
        }
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
            GameApp.APP.getCam().position.set(Cons.VIR_WIDTH / 2, player.getVirY() - Cons.VIR_HEIGHT * 0.2f, 0);
            GameApp.APP.getCam().update();

            b2dcam.position.set(Cons.VIR_WIDTH / 2 / Cons.PPM, player.getVirY() / Cons.PPM - (Cons.VIR_HEIGHT / 7) / Cons.PPM, 0);
            b2dcam.update();

        }


    }

    /**
     * GOTTA IMPLEMENT THIS IN A DIFF WAY CAUSE U GOTTA MAKE SURE YOU STOP THE CURRENT RENDER
     * LOOP WITH A RETURN, SO THAT NOTHING ELSE IS CALLED ONCE EVERYTHING IS DISPOSED
     */

    private void handleBackInput() {
        if (GameApp.APP.isBackPressed && Gdx.input.isKeyPressed(Input.Keys.BACK)){
            isBackStillPressed = true;
        } else{
            isBackStillPressed = false;
            GameApp.APP.isBackPressed = false;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.BACK) && !isBackStillPressed) {
                Gdx.app.exit();
        }
    }

    // GETTERS

    // SETTERS

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
        rain.dispose();
        arrowCharger.dispose();
    }
}