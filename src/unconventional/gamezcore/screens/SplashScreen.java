package unconventional.gamezcore.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import unconventional.gamezcore.game.GameApp;
import unconventional.gamezcore.handlers.Res;
import unconventional.gamezcore.handlers.Cons;

/**
 * Created by RafaeL on 1/8/2017.
 *
 */
public class SplashScreen implements Screen{

    private Texture bg;
    private SpriteBatch sb;

    // effect
    private ParticleEffect effect;
    private float effectWidth = Cons.VIR_WIDTH;
    private float effectHeight = Cons.VIR_HEIGHT;
    private float effectSize= Cons.VIR_WIDTH * 0.1f;
    private String particlePath = "Particles/bgeffects/rainFallEffect.p";

    // make final either in cons or another class that handles all bg effects
    private float x = Cons.VIR_WIDTH / 2;
    private float y = effectHeight / 2;

    public SplashScreen() {
        sb = GameApp.APP.getSb();
        bg = new Texture(Gdx.files.internal("SplashScreen/bg.png"));
        initParticleEffect();
    }

    private void update(){
        if (effect.isComplete())
            effect.reset();

        effect.update(Gdx.graphics.getDeltaTime());
    }

    private void initParticleEffect() {
        effect = new ParticleEffect();
        effect.load(Gdx.files.internal(particlePath), Res.textureAtlas);
        effect.getEmitters().first().setPosition(x, y);
        effect.getEmitters().first().getScale().setHigh(effectSize);
        effect.getEmitters().first().getSpawnWidth().setHigh(effectWidth);
        effect.getEmitters().first().getSpawnHeight().setHigh(effectHeight);
        effect.start();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        update();
        sb.begin();
        sb.setProjectionMatrix(GameApp.APP.getFontcam().combined);
        sb.draw(bg, 0, 0, Cons.VIR_WIDTH, Cons.VIR_HEIGHT);
        effect.draw(sb, Gdx.graphics.getDeltaTime());
        sb.end();
    }

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
        bg.dispose();
        effect.dispose();
    }
}
