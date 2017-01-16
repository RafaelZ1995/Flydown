package core.pickups;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.physics.box2d.World;

import core.handlers.Res;

import static core.handlers.Cons.BALL_DIAM;

/**
 * Created by Rafael on 1/2/2017.
 *
 */
public class ScorePickup extends Pickup {

    private ParticleEffect effect;
    private ParticleEffect barEffect;
    private boolean isBarEffectOn = false;

    /**
     * Constructor
     *
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
        barEffect.draw(sb, Gdx.graphics.getDeltaTime());
    }

    private void initParticleEffect() {
        effect = new ParticleEffect();
        effect.load(Gdx.files.internal("Particles/ScorePickup.p"), Res.textureAtlas);
        effect.getEmitters().first().setPosition(virX, virY);
        effect.getEmitters().first().getScale().setHigh(BALL_DIAM / 2); // well this apperently sets the size of the effects
        effect.start();



        barEffect = new ParticleEffect();
        barEffect.load(Gdx.files.internal("Particles/ScorePickup.p"), Res.textureAtlas);
        barEffect.getEmitters().first().setPosition(virX, virY);
        barEffect.start();

    }

    /**
     * you always have to update the position here apparently.
     */
    private void updatePickupEffect() {
        if (effect.isComplete())
            effect.reset();
        effect.setPosition(virX, virY);
        effect.update(Gdx.graphics.getDeltaTime());

        if (isBarEffectOn) {
            System.out.println("bareffectOn = true");
            if (barEffect.isComplete()){
                System.out.println("bar effect is complete");
                effect.reset();
                isBarEffectOn = false;
            }
            barEffect.setPosition(virX, virY);
            barEffect.update(Gdx.graphics.getDeltaTime());
        }
    }

    // bar effect to show when player picks up a scorepickup
    public void runBarEffectOnce(){
        isBarEffectOn = true;
    }

    @Override
    public void dispose() {
        effect.dispose();
    }

    @Override
    public void reset() {
    }
}
