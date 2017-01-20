package core.particleeffects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;

import core.handlers.Res;
import core.screens.PlayScreen;

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

    // global effect management
    private int currentParticleCount = 5;
    private final int maxParticleCount = 20;
    private String particlePath = "Particles/bgeffects/barEffect2.p";

    private final PlayScreen playScreen;
    private float virX;
    private float virY;

    public BarEffect(PlayScreen playScreen) {
        super();
        initParticleEffect();
        this.playScreen = playScreen;
    }

    private void initParticleEffect() {
        effect = new ParticleEffect();
        effect.load(Gdx.files.internal(particlePath), Res.textureAtlas);
        effect.getEmitters().first().getScale().setHigh(VIR_HEIGHT / 10);
        effect.getEmitters().first().setMaxParticleCount(currentParticleCount);
        effect.start();
    }

    private void update() {
        effect.setPosition(virX, virY);
        effect.update(Gdx.graphics.getDeltaTime());
        int numSquares = Math.min(15, (int) playScreen.getPlaySpeed() * 3);
        effect.getEmitters().first().getEmission().setHigh(numSquares);
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

    public void setCurrentParticleCount(int playSpeed){
        currentParticleCount = playSpeed * 2;
        currentParticleCount = Math.min(currentParticleCount, maxParticleCount);
        effect.getEmitters().first().setMaxParticleCount(currentParticleCount);
    }

    public boolean isComplete(){
        return effect.isComplete();
    }
}
