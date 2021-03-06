package unconventional.gamezcore.pickups;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Pool;

import unconventional.gamezcore.game.GameApp;

/**
 * Created by Rafael on 1/2/2017.
 *
 */
public abstract class Pickup implements Pool.Poolable {

    // -------------------------------------- FINAL VARIABLES --------------------------------------
    protected final int RADIUS = unconventional.gamezcore.handlers.Cons.BALL_DIAM / 2;

    // -------------------------------------- PROTECTED VARIABLES ----------------------------------
    protected Body body;
    protected float virX; // x position
    protected float virY; // y position
    protected SpriteBatch sb;

    // -------------------------------------- PRIVATE VARIABLES ------------------------------------
    private World world;

    // -------------------------------------- PUBLIC METHODS --------------------------------------

    /**
     * Constructor
     * @param world
     * @param initVirX
     * @param initVirY
     */
    public Pickup(World world, float initVirX, float initVirY) {
        this.world = world;
        this.sb = GameApp.APP.getSb();
        this.virX = initVirX;
        this.virY = initVirY;
        construct2d();
    }

    abstract void update();

    abstract void render();

    // -------------------------------------- PRIVATE METHODS --------------------------------------

    private void construct2d() {
        // Define box2d body
        BodyDef bdef = new BodyDef();
        bdef.position.set(virX / unconventional.gamezcore.handlers.Cons.PPM, virY / unconventional.gamezcore.handlers.Cons.PPM);
        bdef.type = BodyDef.BodyType.StaticBody;
        body = world.createBody(bdef);

        // set shape
        CircleShape shape = new CircleShape();
        shape.setRadius(RADIUS / unconventional.gamezcore.handlers.Cons.PPM);

        // Define fixture
        FixtureDef fdef = new FixtureDef();
        fdef.isSensor = true;
        fdef.shape = shape;
        fdef.filter.categoryBits = unconventional.gamezcore.handlers.Cons.BIT_SCOREPICKUP;
        fdef.filter.maskBits = unconventional.gamezcore.handlers.Cons.BIT_PLAYER;

        // Create the actual fixture onto the body
        Fixture fixture = body.createFixture(fdef);

    }


    // -------------------------------------- GETTER METHODS ---------------------------------------
    public float getX() {
        return virX;
    }

    public float getY() {
        return virY;
    }

    public Body getBody() {
        return body;
    }

    // -------------------------------------- SETTER METHODS ---------------------------------------

    /**
     * Wrap around body.setTransform()
     * @param x
     * @param y
     */
    public void setBodyPosition(float x, float y) {
        body.setTransform(x / unconventional.gamezcore.handlers.Cons.PPM, y / unconventional.gamezcore.handlers.Cons.PPM, 0);
        virX = body.getPosition().x * unconventional.gamezcore.handlers.Cons.PPM;
        virY = body.getPosition().y * unconventional.gamezcore.handlers.Cons.PPM;
    }

    abstract void dispose();
}