package unconventional.gamezcore.Hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import unconventional.gamezcore.objects.Ball;
import unconventional.gamezcore.handlers.Cons;

/**
 * Created by Rafael on 12/29/2016.
 * Learned: you gotta decrease the "duration" of the effect in order to do smooth effects as you change
 * the spanwidth of the effect from inside the code.
 */
public class ArrowCharger {

    // make them final in Cons later
    private float x = Cons.VIR_WIDTH / 2;
    private float y = Cons.VIR_HEIGHT - Cons.VIR_HEIGHT / 10;
    private float arrowPngWidth = Cons.VIR_WIDTH / 2;
    private float arrowPngheight = Cons.VIR_HEIGHT / 10;
    private float arrowPointWidth = arrowPngWidth / 3;

    ParticleEffect effect; // one particle effect for both leftward and rightward charge
    private float effectSize = arrowPngheight / 7;
    private float playerxRatio;

    private SpriteBatch sb;
    private Ball ball;

    /**
     * Constructor
     */
    public ArrowCharger(Ball ball) {
        sb = unconventional.gamezcore.game.GameApp.APP.getSb();
        this.ball = ball;
        initParticleEffect();
    }

    /**
     * Initialize both the particle effect
     */
    private void initParticleEffect() {
        effect = new ParticleEffect();
        effect.load(Gdx.files.internal("Particles/arrowCharge.p"), unconventional.gamezcore.handlers.Res.textureAtlas);
        effect.getEmitters().first().setPosition(x, y + arrowPngheight /2);
        effect.getEmitters().first().getScale().setHigh(effectSize);
        effect.start();
    }

    public void update() {
        playerxRatio = ball.getxForceRatio();
        float resizeBy = playerxRatio * arrowPngWidth * 0.68f;
        updateChargeEffect(resizeBy);

        if (effect.isComplete())
            effect.reset();

        effect.update(Gdx.graphics.getDeltaTime());
    }

    /**
     * @param resizeBy will make the charge's arrowPngWidth this size.
     */
    private void updateChargeEffect(float resizeBy) {
        if (ball.isLeftSideTouchDown())
            effect.getEmitters().first().getSpawnWidth().setHigh(-resizeBy, -resizeBy);
        else
            effect.getEmitters().first().getSpawnWidth().setHigh(resizeBy, resizeBy);
    }

    public void render() {
        update();
        sb.setColor(unconventional.gamezcore.game.GameApp.CurrentThemeColor);
        sb.draw(unconventional.gamezcore.handlers.Res.arrowRegion, x, y, -arrowPngWidth, arrowPngheight); // left arrow
        sb.draw(unconventional.gamezcore.handlers.Res.arrowRegion, x, y, arrowPngWidth, arrowPngheight); // right arrow

        // effect on top of arrows
        effect.draw(sb, Gdx.graphics.getDeltaTime());

    }

    public void dispose(){
        effect.dispose();
    }
}