package unconventional.gamezcore.Hud;

        import com.badlogic.gdx.Gdx;
        import com.badlogic.gdx.graphics.g2d.ParticleEffect;
        import com.badlogic.gdx.graphics.g2d.SpriteBatch;

        import unconventional.gamezcore.handlers.Cons;

/**
 * Created by Rafael on 12/30/2016.
 *
 */
public class Rain {

    // global effect management
    private int currentParticleCount = 3;
    private final int maxParticleCount = 20;
    private boolean isUpdateNeeded = false;

    //private
    private ParticleEffect effect;
    private float effectWidth = Cons.VIR_WIDTH;
    private float effectHeight = Cons.VIR_HEIGHT;
    private float effectSize= Cons.VIR_WIDTH * 0.1f;

    // make them final in Cons later+
    private float x = Cons.VIR_WIDTH / 2;
    private float y = effectHeight / 2;

    private SpriteBatch sb;
    private String particlePath = "Particles/bgeffects/rainFallEffect.p";

    public Rain() {
        sb = unconventional.gamezcore.game.GameApp.APP.getSb();
        initParticleEffect();
    }

    private void initParticleEffect() {
        effect = new ParticleEffect();
        effect.load(Gdx.files.internal(particlePath), unconventional.gamezcore.handlers.Res.textureAtlas);
        effect.getEmitters().first().setPosition(x, y);
        effect.getEmitters().first().getScale().setHigh(effectSize);
        effect.getEmitters().first().getSpawnWidth().setHigh(effectWidth);
        effect.getEmitters().first().getSpawnHeight().setHigh(effectHeight);
        effect.getEmitters().first().setMaxParticleCount(currentParticleCount);
        effect.getEmitters().first().setContinuous(false);
        effect.start();
    }

    public void setMaxEffectParticles(int playSpeed){
        isUpdateNeeded = true;
        currentParticleCount = playSpeed * 2;
    }

    public void forceMaxEffectParticles(int n){
        currentParticleCount = n;
        effect.getEmitters().first().setMaxParticleCount(n);
    }

    private void update(){
        //System.out.println("rainParticleCount: " + effect.getEmitters().first().getMaxParticleCount());


        // only runs if effect is not continuos
        if (effect.isComplete()) {
            currentParticleCount = Math.min(currentParticleCount, maxParticleCount);
            effect.getEmitters().first().setMaxParticleCount(currentParticleCount);
            effect.reset();
        }

        effect.update(Gdx.graphics.getDeltaTime());
    }

    public void render(){
        update();
        effect.draw(sb, Gdx.graphics.getDeltaTime());
    }

    public void dispose(){
        effect.dispose();
    }
}
