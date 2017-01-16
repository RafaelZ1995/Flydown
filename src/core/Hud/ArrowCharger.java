package core.Hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import core.game.GameApp;
import core.handlers.Res;
import core.objects.Ball;
import core.screens.PlayScreen;

import static core.handlers.Cons.VIR_HEIGHT;
import static core.handlers.Cons.VIR_WIDTH;

/**
 * Created by Rafael on 12/29/2016.
 * Learned: you gotta decrease the "duration" of the effect in order to do smooth effects as you change
 * the spanwidth of the effect from inside the code.
 */
public class ArrowCharger {

    // make them final in Cons later
    private float x = VIR_WIDTH / 2;
    private float y = VIR_HEIGHT - VIR_HEIGHT / 10;
    private float arrowPngWidth = VIR_WIDTH / 2;
    private float arrowPngheight = VIR_HEIGHT / 10;
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
        sb = GameApp.APP.getSb();
        this.ball = ball;
        initParticleEffect();
    }

    /**
     * Initialize both the particle effect
     */
    private void initParticleEffect() {
        effect = new ParticleEffect();
        effect.load(Gdx.files.internal("Particles/arrowCharge.p"), Res.textureAtlas);
        effect.getEmitters().first().setPosition(x, y + arrowPngheight /2);
        effect.getEmitters().first().getScale().setHigh(effectSize);
        System.out.println(effect.getEmitters().first().getScale().getHighMax());
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
        sb.setColor(GameApp.CurrentThemeColor);
        sb.draw(Res.arrowRegion, x, y, -arrowPngWidth, arrowPngheight); // left arrow
        sb.draw(Res.arrowRegion, x, y, arrowPngWidth, arrowPngheight); // right arrow

        // effect on top of arrows
        effect.draw(sb, Gdx.graphics.getDeltaTime());

    }

    public void dispose(){
        effect.dispose();
    }
}