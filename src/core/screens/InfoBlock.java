package core.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;

import core.game.GameApp;
import core.handlers.Res;

import static core.handlers.Cons.VIR_HEIGHT;
import static core.handlers.Cons.VIR_WIDTH;

/**
 * Created by Rafael on 1/18/2017.
 * how about we make a util class to render these info blocks? like a static one.
 */
public class InfoBlock {

    private final Sprite infoButton;
    private Sprite block;
    PermanentText[] lines;


    public InfoBlock() {
        block = new Sprite(Res.wallRegion);
        block.setSize(VIR_WIDTH, VIR_HEIGHT);
        block.setPosition(0, 0);
        block.setColor(Color.BLACK);

        lines = new PermanentText[5];

        lines[0] = new PermanentText("Hey! Thanks for playing my game!", "64");
        lines[1] = new PermanentText("anything you want me to add/change?", "64");
        lines[2] = new PermanentText("hit me up on snap-chat and tell me!!", "64");
        lines[3] = new PermanentText("snap-chat: zunigasan ", "64");
        lines[4] = new PermanentText("if it's possible, I'll add it!", "64");

        for (int i = 0; i < lines.length; i++){
            lines[i].setPos(VIR_WIDTH / 2, VIR_HEIGHT * 0.8f - (VIR_HEIGHT * 0.1f) * i);
        }

        // info button
        infoButton = new Sprite(Res.infoButtonRegion);
        infoButton.setSize(VIR_WIDTH * 0.15f, VIR_WIDTH * 0.15f);
        infoButton.setOrigin(VIR_WIDTH * 0.1f / 2, VIR_WIDTH * 0.1f / 2);
        infoButton.setPosition(VIR_WIDTH / 2 - infoButton.getWidth() / 2, VIR_HEIGHT * 0.1f);
        infoButton.setColor(GameApp.CurrentThemeColor);
    }

    public void render() {
        block.draw(GameApp.APP.getSb());
        for (PermanentText p : lines)
            p.render();
        infoButton.draw(GameApp.APP.getSb());
    }
}