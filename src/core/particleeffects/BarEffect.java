package core.particleeffects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;

import core.handlers.Res;

import static core.handlers.Cons.VIR_HEIGHT;
import static core.handlers.Cons.VIR_WIDTH;

/**
 * Created by Rafael on 1/6/2017.
 *
 * @@@ I CAN PROBABLY CHANGE THE SIZE OF THE TEXTURE IN RELATION TO THE USER'S RESOLUTION
 * AND THEN USE THAT AS THE PARTICLE PNG SO THAT IT SCALES CORRECTLY AS A THIN COLUMN
 * IN EVERY DEVICE
 * Awesome effects: barEffect2, sqrEffect2
 */
public class BarEffect extends MyEffect {

    private float virX;
    private float virY;

    public BarEffect() {
        super();
        initParticleEffect();
    }

    private void initParticleEffect() {
        effect = new ParticleEffect();
        //effect.load(Gdx.files.internal("Particles/bgeffects/3dtriEffect.p"), Gdx.files.internal(""));
        effect.load(Gdx.files.internal("Particles/bgeffects/barEffect2.p"), Res.textureAtlas);
        //effect.getEmitters().first().setMaxParticleCount(15);
        effect.getEmitters().first().getScale().setHigh(VIR_HEIGHT / 10);
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

    public void setPosition(float virX, float virY) {
        this.virX = virX;
        this.virY = virY;
    }

    public boolean isComplete(){
        return effect.isComplete();
    }
}
