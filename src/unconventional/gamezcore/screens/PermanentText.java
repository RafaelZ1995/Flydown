package unconventional.gamezcore.screens;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

import unconventional.gamezcore.game.GameApp;
import unconventional.gamezcore.handlers.Res;

/**
 * Created by Rafael on 1/13/2017.
 *
 */
public class PermanentText {
    private GlyphLayout layout;
    private String text;
    private float x, y;
    private BitmapFont currentFont;

    public PermanentText(String text, float x, float y, String whichFont) {
        this.text = text;
        if (whichFont.equals("128")) {
            layout = new GlyphLayout(Res.font128, text);
            currentFont = Res.font128;
        }
        else if (whichFont.equals("64")) {
            layout = new GlyphLayout(Res.font64, text);
            currentFont = Res.font64;
        }
        else{
            //-------------FONT DOES NOT EXIST------------");
        }

        this.x = x - layout.width / 2;
        this.y = y - layout.height / 2;
    }

    public PermanentText(String text, String whichFont) {
        this.text = text;
        if (whichFont.equals("128")) {
            layout = new GlyphLayout(Res.font128, text);
            currentFont = Res.font128;
        }
        else if (whichFont.equals("64")) {
            layout = new GlyphLayout(Res.font64, text);
            currentFont = Res.font64;
        } else{
            //("--------------FONT DOES NOT EXIST------------");
        }
    }

    public void setPos(float x, float y){
        this.x = x - layout.width / 2;
        this.y = y - layout.height / 2;
    }

    public void render(){
        currentFont.draw(GameApp.APP.getSb(), layout, x, y);
    }
}
