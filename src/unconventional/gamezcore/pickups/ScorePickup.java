package unconventional.gamezcore.pickups;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.physics.box2d.World;

import unconventional.gamezcore.handlers.Res;
import unconventional.gamezcore.handlers.Cons;

/**
 * Created by Rafael on 1/2/2017.
 *
 */
public class ScorePickup extends Pickup {

    private ParticleEffect effect;

    /**
     * Constructor
     * @param world
     * @param initVirX
     * @param initVirY
     */
    public ScorePickup(World world, float initVirX, float initVirY) {
        super(world, initVirX, initVirY);
        body.getFixtureList().first().setUserData(this);
        initParticleEffect();
    }

    public void update() {
        updatePickupEffect();
    }

    public void render() {
        update();
        effect.draw(sb, Gdx.graphics.getDeltaTime());
    }

    private void initParticleEffect() {
        effect = new ParticleEffect();
        effect.load(Gdx.files.internal("Particles/ScorePickup.p"), Res.textureAtlas);
        effect.getEmitters().first().setPosition(virX, virY);
        effect.getEmitters().first().getScale().setHigh(Cons.BALL_DIAM / 2); // well this apperently sets the size of the effects
        effect.start();
    }

    /**
     * you always have to update the position here apparently.
     */
    private void updatePickupEffect() {
        effect.setPosition(virX, virY);
        effect.update(Gdx.graphics.getDeltaTime());
    }

    /*
    * of effect too
     */
    public void setPosition(float x, float y){
        body.setTransform(x / Cons.PPM, y / Cons.PPM, 0);
        virX = body.getPosition().x * Cons.PPM;
        virY = body.getPosition().y * Cons.PPM;
        effect.setPosition(virX, virY);
    }

    @Override
    public void dispose() {
        effect.dispose();
    }

    @Override
    public void reset() {
        setBodyPosition(0, 0);
        effect.setPosition(0, 0);
        virX = 0;
        virY = 0;
    }
}
