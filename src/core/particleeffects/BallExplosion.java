package core.particleeffects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;

import core.handlers.Res;

import static core.handlers.Cons.VIR_HEIGHT;
import static core.handlers.Cons.VIR_WIDTH;

/**
 * Created by Rafae on 1/11/2017.
 */
public class BallExplosion extends MyEffect {

    private float virX;
    private float virY;

    public BallExplosion() {
        super();
        initParticleEffect();
    }

    private void initParticleEffect() {
        effect = new ParticleEffect();
        effect.load(Gdx.files.internal("Particles/ballExplosion.p"), Res.textureAtlas);
        effect.start();
    }

    private void update() {
        effect.setPosition(virX, virY);
        effect.update(Gdx.graphics.getDeltaTime());
    }

    @Override
    public void render() {
        update();
        effect.draw(sb, Gdx.graphics.getDeltaTime());
    }

    @Override
    public void dispose() {
        effect.dispose();
    }

    @Override
    public void reset() {
        effect.reset();
    }

    public void setPosition(float virX, float virY){
        this.virX = virX;
        this.virY = virY;
    }

    public boolean isComplete(){
        return effect.isComplete();
    }
}
