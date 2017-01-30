package unconventional.gamezcore.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;

import unconventional.gamezcore.game.GameApp;
import unconventional.gamezcore.handlers.Res;
import unconventional.gamezcore.handlers.Cons;

/**
 * Created by Rafael on 1/16/2017.
 *
 */
public class Tutorial {

    // tutorial
    private Sprite leftScreen;
    private Sprite rightScreen;
    private boolean leftIsFading = false;
   // private boolean rightIsFading = false;

    private Sprite leftHand;
    private Sprite rightHand;

    private PermanentText leftText1;
    private PermanentText leftText2;
    private PermanentText leftText3;
    private PermanentText rightText1;
    private PermanentText rightText2;
    private PermanentText rightText3;

    private float fadeSpeed = 0.005f;
    private boolean leftOn = true;
    private float leftAlpha = 0;
    private boolean leftFadingIn = true;
    private boolean rightFadingIn = false;
    private float rightAlpha = 0;

    public Tutorial() {
        // Tutorial
        leftScreen = new Sprite(Res.wallRegion);
        leftScreen.setSize(Cons.VIR_WIDTH / 2, Cons.VIR_HEIGHT);
        leftScreen.setPosition(0, 0);
        leftScreen.setColor(Color.WHITE);
        leftScreen.setAlpha(0.5f);

        rightScreen = new Sprite(Res.wallRegion);
        rightScreen.setSize(Cons.VIR_WIDTH / 2, Cons.VIR_HEIGHT);
        rightScreen.setPosition(Cons.VIR_WIDTH / 2, 0);
        rightScreen.setColor(Color.WHITE);
        rightScreen.setAlpha(0f);

        leftHand = new Sprite(Res.tapRegion);
        leftHand.setPosition(Cons.VIR_WIDTH * 0.25f - leftHand.getWidth() / 2, Cons.VIR_HEIGHT * 0.15f);
        leftHand.setColor(Color.BLACK);
        leftHand.flip(true, false);

        rightHand = new Sprite(Res.tapRegion);
        rightHand.setColor(Color.BLACK);
        rightHand.setPosition(Cons.VIR_WIDTH * 0.75f - rightHand.getWidth() / 2, Cons.VIR_HEIGHT * 0.15f);

        leftText1 = new PermanentText("hold", Cons.VIR_WIDTH * 0.25f, Cons.VIR_HEIGHT * 0.14f, "64");
        leftText2 = new PermanentText("charge", Cons.VIR_WIDTH * 0.25f, Cons.VIR_HEIGHT * 0.11f, "64");
        leftText3 = new PermanentText("release", Cons.VIR_WIDTH * 0.25f, Cons.VIR_HEIGHT * 0.08f, "64");

        rightText1 = new PermanentText("hold", Cons.VIR_WIDTH * 0.75f, Cons.VIR_HEIGHT * 0.14f, "64");
        rightText2 = new PermanentText("charge", Cons.VIR_WIDTH * 0.75f, Cons.VIR_HEIGHT * 0.11f, "64");
        rightText3 = new PermanentText("release", Cons.VIR_WIDTH * 0.75f, Cons.VIR_HEIGHT * 0.08f, "64");
    }

    /**
     * the code for fading in and out the left and right side of the screen
     */
    public void updateHalfies(){
        if (leftOn) {

            if (leftFadingIn) {
                leftAlpha += fadeSpeed;

                if (leftAlpha > 0.5f)
                    leftFadingIn = false;
            } else {
                leftAlpha -= fadeSpeed;

                if (leftAlpha < 0){ // switch to right half
                    leftOn = false;
                    rightFadingIn = true;
                    return;
                }
            }

            leftScreen.setAlpha(leftAlpha);
            leftScreen.draw(GameApp.APP.getSb());

        } else {
            if (rightFadingIn) {
                rightAlpha += fadeSpeed;

                if (rightAlpha > 0.5f)
                    rightFadingIn = false;
            } else {
                rightAlpha -= fadeSpeed;

                if (rightAlpha < 0){
                    leftOn = true;
                    leftFadingIn = true;
                    return;
                }
            }

            rightScreen.setAlpha(rightAlpha);
            rightScreen.draw(GameApp.APP.getSb());
        }
    }

    public void render(){
        updateHalfies();
        leftHand.draw(GameApp.APP.getSb());
        rightHand.draw(GameApp.APP.getSb());

        leftText1.render();
        leftText2.render();
        leftText3.render();

        rightText1.render();
        rightText2.render();
        rightText3.render();
    }
}