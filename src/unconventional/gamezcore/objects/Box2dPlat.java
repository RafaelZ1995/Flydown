package unconventional.gamezcore.objects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Pool;

import unconventional.gamezcore.game.GameApp;

/**
 * Created by Rafael on 1/1/2017.
 *
 */
public abstract class Box2dPlat implements Pool.Poolable{

    // private
    protected float virX; // x position
    protected float virY; // y position
    protected float width;
    protected float height;
    protected Sprite sprite;
    protected Body body;
    private World world;
    protected SpriteBatch sb;

    protected Box2dPlat(World world, int width, int height, float initVirX, float initVirY) {
        this.world = world;
        this.width = width;
        this.height = height;
        this.sb = GameApp.APP.getSb();
        this.virX = initVirX;
        this.virY = initVirY;
        construct2d();

    }


    public void construct2d() {
        // Define box2d body
        BodyDef bdef = new BodyDef();
        // (width/2)/PPM so that (x,y) are at top left corner, same thing for height
        bdef.position.set(virX / unconventional.gamezcore.handlers.Cons.PPM + (width / 2 / unconventional.gamezcore.handlers.Cons.PPM), virY / unconventional.gamezcore.handlers.Cons.PPM - (height / 2 / unconventional.gamezcore.handlers.Cons.PPM));
        bdef.type = BodyDef.BodyType.KinematicBody;
        body = world.createBody(bdef);

        // set shape
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2 / unconventional.gamezcore.handlers.Cons.PPM, height / 2 / unconventional.gamezcore.handlers.Cons.PPM); // divided by 2 on purpose

        // Define fixture
        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.restitution = 0.5f;
        fdef.filter.categoryBits = unconventional.gamezcore.handlers.Cons.BIT_PLAT;
        fdef.filter.maskBits = unconventional.gamezcore.handlers.Cons.BIT_PLAYER;

        // Create the actual fixture onto the body
        Fixture fixture = body.createFixture(fdef);
    }


    abstract void update();

    abstract void render();

    public Body getBody() {
        return body;
    }

    public float getX() {
        return virX;
    }

    public float getY() {
        return virY;
    }

    /**
     * Wrap around body.setTransform()
     * @param x
     * @param y
     */
    public void setBodyPosition(float x, float y) {
        body.setTransform(x / unconventional.gamezcore.handlers.Cons.PPM + (width / 2 / unconventional.gamezcore.handlers.Cons.PPM), y / unconventional.gamezcore.handlers.Cons.PPM - (height / 2 / unconventional.gamezcore.handlers.Cons.PPM), 0);
        // also update virX and virY right after
        virX = body.getPosition().x * unconventional.gamezcore.handlers.Cons.PPM - width / 2; // revert the additions done when setting position of the body in construct2d()
        virY = body.getPosition().y * unconventional.gamezcore.handlers.Cons.PPM + height / 2; //  revert the substraction done when setting position of the body in construct2d()
    }

    /**
     * Wrap around body.setAngularVelocity()
     * @param f
     */
    public void setAngularVel(float f){
        body.setAngularVelocity(f);
    }

    public abstract void setSize(float width, float height);

    public void dispose(){

    }

    public float getWidth() {
        return width;
    }
}