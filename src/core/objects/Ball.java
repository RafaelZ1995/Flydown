package core.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import core.game.GameApp;
import core.handlers.Cons;
import core.handlers.Res;
import core.screens.PlayScreen;

import static core.handlers.Cons.PPM;
import static core.handlers.Cons.BALL_DIAM;
import static core.handlers.Cons.BALL_X_FORCE;
import static core.handlers.Cons.FREE_ROAM;
import static core.handlers.Cons.TIME_TO_TRAVEL_1_DEPTH;
import static core.handlers.Cons.VIR_HEIGHT;
import static core.handlers.Cons.VIR_WIDTH;


/**
 * Created by Rafael on 12/29/2016.
 * Player class but going to implement different input method (hold based)
 */
public class Ball implements InputProcessor {

    // global effect management
    private final int initTailLife = 1000;
    private final int maxTailLife = 6000;
    private float tailLifeMultiplier = 600;
    private float dt;

    private final PlayScreen playScreen;
    private float timeToTravel1Depth = 7000; // 6 seconds
    private float fallSpeed = -(VIR_HEIGHT / timeToTravel1Depth) * 10;
    private boolean fallSpeedIsUpdated;

    // params
    private World world;
    private SpriteBatch sb;

    // Properties
    private final int RADIUS = BALL_DIAM / 2;
    private float virX; // virtual coordinates
    private float virY; // virtual coordinates
    private Sprite sprite;
    private Body body;
    private Vector2 pos; // param, scaled 1/100 for construct2d()
    private boolean crashed = false;

    // growth "poison"
    private boolean growthPoisoned = false; // doesnt work well.
    private float radiusGrowth = 0; // amount to add to the body's RADIUS every second
    private float TotalCurrentRadius = RADIUS + radiusGrowth;


    // PARTICLE EFFECTS
    private ParticleEffect effect;  // Change the "life" property of the effect to change how long the tail
    private ParticleEffect effect2;
    private boolean effect1First;

    // variables for hold-based input
    private float maxHoldTime = 500; // in milliseconds
    private float currentHoldTime; // in milliseconds
    private long holdStartTime;
    private boolean leftSideTouchDown;
    private float xForceRatio;
    private boolean touchedDown = false;
    private boolean bodyDestroyed = false;
    private float maxXSpeed = ((float) VIR_WIDTH / 1500) * 10;

    public Ball(World world, PlayScreen playScreen, Vector2 pos) {
        this.world = world;
        this.pos = pos;
        this.sb = GameApp.APP.getSb();
        this.playScreen = playScreen;
        virX = pos.x;
        virY = pos.y;
        pos.scl(1 / PPM);
        xForceRatio = 0;
        construct2d();
        Gdx.input.setInputProcessor(this);

        initParticleEffect();
    }

    private void initParticleEffect() {
        // TESTING PARTICLE EFFECTS
        effect = new ParticleEffect();
        effect.load(Gdx.files.internal("Particles/ballTail2.p"), Res.textureAtlas);
        effect.getEmitters().first().getScale().setHigh(BALL_DIAM / 4);
        effect.getEmitters().first().setPosition(virX, virY);
        effect.getEmitters().first().getLife().setHigh(initTailLife);
        effect.getEmitters().first().setAdditive(false);
        effect.start();

        effect2 = new ParticleEffect();
        effect2.load(Gdx.files.internal("Particles/ballTail2.p"), Res.textureAtlas);
        effect2.getEmitters().first().getScale().setHigh(BALL_DIAM / 4);
        effect2.getEmitters().first().getTint().setColors(new float[]{1f, 0, 0});
        effect2.getEmitters().first().setPosition(virX, virY);
        effect2.getEmitters().first().getLife().setHigh(initTailLife);
        effect2.getEmitters().first().setAdditive(false);
        effect2.start();
    }

    /**
     * Update
     */

    public void update() {
        updateGrowthPoison();

        // update body positionb
        if (body.getLinearVelocity().x > maxXSpeed)
            body.setLinearVelocity(maxXSpeed, fallSpeed);
        else if (body.getLinearVelocity().x < -maxXSpeed)
            body.setLinearVelocity(-maxXSpeed, fallSpeed);
        else
            body.setLinearVelocity(body.getLinearVelocity().x, fallSpeed);

        // update position
        virX = body.getPosition().x * PPM;
        virY = body.getPosition().y * PPM;

        updateTailEffect(); // 2d particle effect

        if (FREE_ROAM)
            updateLaunch(); // updating hold down time
    }

    private void updateSpeed() {
            timeToTravel1Depth = TIME_TO_TRAVEL_1_DEPTH - playScreen.getPlaySpeed() * 300; // 6 seconds
            if (timeToTravel1Depth < 3200)
                timeToTravel1Depth = 3200;
            //System.out.println("fallSpeedInTime: " + timeToTravel1Depth);
            fallSpeed = -(VIR_HEIGHT / timeToTravel1Depth) * 10;
    }

    /**
     * updateTailEffect
     */
    private void updateTailEffect() {
        dt += Gdx.graphics.getDeltaTime() * 1.5f;
        //System.out.println("playSpeed: " + playScreen.getPlaySpeed() + "score: " + playScreen.getScore());
        if (Math.sin(dt) > 0f) {
            effect1First = true;
        }

        if (Math.sin(dt) < -0f) {
            effect1First = false;
        }

        float tempx = virX + (float) Math.sin(dt) * BALL_DIAM / 3;
        float tempx2 = virX + ((float) -Math.sin(dt)) * BALL_DIAM / 3;

        effect.setPosition(tempx, virY);
        effect.update(Gdx.graphics.getDeltaTime());

        effect2.setPosition(tempx2, virY);
        effect2.update(Gdx.graphics.getDeltaTime());
    }

    public void setTailLife(int playSpeed){
        float tailLife = tailLifeMultiplier * playSpeed;
        tailLife = Math.min(tailLife, maxTailLife);
        effect.getEmitters().first().getLife().setHigh(tailLife);
        effect2.getEmitters().first().getLife().setHigh(tailLife);
    }

    /**
     * Update Launch
     * I could also just use the boolean that keeps track of the finger being down, and just increase the xForceRatio however i want as long as the boolean is true.
     */
    private void updateLaunch() {
        if (touchedDown) {
            currentHoldTime = System.currentTimeMillis() - holdStartTime;
            if (currentHoldTime > maxHoldTime)
                currentHoldTime = maxHoldTime;

        }
        xForceRatio = currentHoldTime / maxHoldTime;
    }

    private void updateGrowthPoison() {
        if (growthPoisoned) {
            radiusGrowth += 0.3f;
        }
        TotalCurrentRadius = RADIUS + radiusGrowth;
        body.getFixtureList().first().getShape().setRadius(TotalCurrentRadius / PPM);
    }


    /**
     * Render
     */
    public void render() {
        update();

        if (effect1First){
            effect.draw(sb, Gdx.graphics.getDeltaTime());
            effect2.draw(sb, Gdx.graphics.getDeltaTime());

        } else {
            effect2.draw(sb, Gdx.graphics.getDeltaTime());
            effect.draw(sb, Gdx.graphics.getDeltaTime());
        }
        sb.setColor(GameApp.CurrentThemeColor);
        sb.draw(Res.playerRegion, body.getPosition().x * PPM - TotalCurrentRadius,
                body.getPosition().y * PPM - TotalCurrentRadius,
                TotalCurrentRadius * 2,
                TotalCurrentRadius * 2);
    }

    /**
     * Construct2d
     */
    private void construct2d() {
        // Define box2d body
        BodyDef bdef = new BodyDef();
        bdef.position.set(pos);
        bdef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bdef);

        // set shape
        CircleShape shape = new CircleShape();
        shape.setRadius(RADIUS / PPM);


        // Define fixture
        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.restitution = 1f;
        fdef.friction = 0f;
        fdef.filter.categoryBits = Cons.BIT_PLAYER;
        fdef.filter.maskBits = Cons.BIT_PLAT | Cons.BIT_PLAYER | Cons.BIT_SCOREPICKUP;

        // Create the actual fixture onto the body
        Fixture fixture = body.createFixture(fdef);
        fixture.setUserData(this); // to recognize in contact listener
    }


    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        touchedDown = true;
        if (FREE_ROAM)
            touchDownFreeRoam(screenX);

        // graphMovementSystem
        if (touchDownLeftSide(screenX)) {
            leftSideTouchDown = true;
        } else {
            leftSideTouchDown = false;
        }
        return false;
    }


    /*
     * so this is the method is called right after hitting the play button
     * so that the mainscreen takes you to the playscreen
     */
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {

        if (touchedDown == false)
            return false;
        else
            touchedDown = false;

        if (FREE_ROAM)
            touchUpFreeRoam();

        return false;
    }

    private boolean touchDownLeftSide(int screenX) {
        if (screenX < Gdx.graphics.getWidth() / 2)
            return true;
        else
            return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }


    // getters

    // virtual coordinates (already multiplied * PPM)
    public float getVirX() {
        return virX;
    }

    public float getVirY() {
        return virY;
    }

    public Body getBody() {
        return body;
    }

    public float getxForceRatio() {
        return xForceRatio;
    }

    public boolean isLeftSideTouchDown() {
        return leftSideTouchDown;
    }


    /**
     * Dispose
     */
    public void dispose() {
        effect.dispose();
    }


    // ------------------------------INPUT HELPER METHODS----------------------------------------

    // Free roam touch input methods, for touchDown, touchUp
    private void touchDownFreeRoam(int screenX) {
        touchedDown = true;

        holdStartTime = System.currentTimeMillis();
        if (touchDownLeftSide(screenX)) {
            leftSideTouchDown = true;
        } else {
            leftSideTouchDown = false;
        }
    }

    private void touchUpFreeRoam() {
        touchedDown = false;
        currentHoldTime = 0;

        // avoids the input problem when transitioning from main to play screen
        if (holdStartTime == 0) {
            return;
        }

        if (leftSideTouchDown) {
            body.applyForceToCenter(-xForceRatio * BALL_X_FORCE, 0, true);
        } else {
            body.applyForceToCenter(xForceRatio * BALL_X_FORCE, 0, true);
        }
    }

    public void resetExtraRadiusGrowth() {
        radiusGrowth = 0;
    }

    public void setPosition(float x, float y) {
        body.setTransform(x / PPM, y / PPM, 0);
    }

    public void setCrashed(boolean crashed) {
        this.crashed = crashed;
    }

    public boolean isCrashed() {
        return crashed;
    }

    public boolean isBodyDestroyed() {
        return bodyDestroyed;
    }

    public void destroy() {
        world.destroyBody(body);
        dispose();
        bodyDestroyed = true;
    }
}