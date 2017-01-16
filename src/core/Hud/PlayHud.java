package core.Hud;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import core.game.GameApp;
import core.handlers.Res;
import core.screens.PlayScreen;

import static core.handlers.Cons.VIR_HEIGHT;
import static core.handlers.Cons.VIR_WIDTH;

/**
 * Created by Rafae on 12/26/2016.
 *
 * @@ YOU MOST DEFINITELY NEED TO INITIALIZE THE BMFONT IN THE GAME CLASS NOT HERE.
 *
 * Class will handle the Score, pause button, and any other things that would be considered
 * part of the "hud" in the playscreen
 */
public class PlayHud {

    // private
    private PlayScreen playScreen; // ((PlayScreen) (APP.getScreen())).getScore() could get like this
    private SpriteBatch sb;
    private int score;
    private final float scoreX = VIR_WIDTH/2;
    private final float scoreY = VIR_HEIGHT - VIR_HEIGHT/10;

    // add in ArrowCharger and Rain here?
    private ArrowCharger arrowCharger;
    private Rain rain;

    // arrow charger
   // private ArrowCharger arrowCharger;

    public PlayHud(PlayScreen playScreen) {
        this.sb = GameApp.APP.getSb();
        this.playScreen = playScreen;

        // hud camera elements
        arrowCharger = new ArrowCharger(playScreen.getPlayer());
        rain = new Rain();
    }

    /**
     * The FONT CAMERA is set in the PlayScreen's render method
     * still gotta make that 56 in a relation to VIR width or height
     */
    public void render(){

        // score rendering
        int depth = playScreen.getScore();
        String depthString = String.valueOf(depth);
        Res.font128.getData().setScale(1f, 1f);
        Res.font128.draw(sb, depthString, scoreX - 56, scoreY); // font camera already set

        arrowCharger.render();
        rain.render();
    }

    public void dispose(){
        arrowCharger.dispose();
        rain.dispose();
    }

    public Rain getRain() {
        return rain;
    }
}
