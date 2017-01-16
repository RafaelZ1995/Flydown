package core.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import core.game.GameApp;

/**
 * Created by Rafael on 10/5/2016.
 * Load resources such as images for textures and sounds
 */
public class Res {

    // Saving data
    public static Preferences prefs;
    public static int highScore;

    public static BitmapFont font128;

    public static TextureAtlas textureAtlas;
    public static TextureRegion platRegion;
    public static TextureRegion wallRegion;
    public static TextureRegion arrowRegion;
    public static TextureRegion playerRegion;
    public static TextureRegion barEffectRegion;
    public static TextureRegion playButtonRegion;
    public static TextureRegion restartButtonRegion;

    public Res() {

        // get font from pngs
        font128 = new BitmapFont(Gdx.files.internal("Font/roboto128.fnt"));

        textureAtlas = new TextureAtlas(Gdx.files.internal("Atlas/MainAtlas.atlas"));
        platRegion = textureAtlas.findRegion("PlatTile");
        wallRegion = textureAtlas.findRegion("WallTile");
        arrowRegion = textureAtlas.findRegion("BlackArrow");
        playerRegion = textureAtlas.findRegion("Ball");
        barEffectRegion = textureAtlas.findRegion("BarEffectBlock");

        // UI
        playButtonRegion = textureAtlas.findRegion("Play");
        restartButtonRegion = textureAtlas.findRegion("Restart");


        // high score
        prefs = Gdx.app.getPreferences("My Preferences");
        highScore = prefs.getInteger("bestscore", 0);

    }

    public void dispose(){
        textureAtlas.dispose();
    }

    public static void updateBestScore(int currentScore) {
        int bestScore = prefs.getInteger("bestscore");
        if (currentScore > bestScore){
            prefs.putInteger("bestscore", currentScore);
            prefs.flush();
        }

    }
}