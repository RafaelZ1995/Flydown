package unconventional.gamezcore.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

/**
 * Created by Rafael on 10/5/2016.
 * Load resources such as images for textures and sounds
 */
public class Res {

    // Saving data
    public static Preferences prefs;
    public static int highScore;

    public static BitmapFont font128;
    public static BitmapFont font64;
    public static int userFontSizeBig = (Cons.VIR_WIDTH * 128) / 1080;
    public static int userFontSizeSmall = (Cons.VIR_WIDTH * 48) / 1080;

    private boolean generateFont = true;

    public static TextureAtlas textureAtlas;
    public static TextureRegion platRegion;
    public static TextureRegion wallRegion;
    public static TextureRegion arrowRegion;
    public static TextureRegion playerRegion;
    public static TextureRegion barEffectRegion;
    public static TextureRegion playButtonRegion;
    public static TextureRegion restartButtonRegion;
    public static TextureRegion infoButtonRegion;
    public static TextureRegion infoPlusRegion;
    public static TextureRegion tapRegion;
    public static TextureRegion leaderboardRegion;



    public Res() {

        // get font from pngs
        if (generateFont){
            font128 = generateFontASyn(userFontSizeBig);
            font128.setUseIntegerPositions(false);

            font64 = generateFontASyn(userFontSizeSmall);
            font64.setUseIntegerPositions(false);
        } else {
            font128 = new BitmapFont(Gdx.files.internal("Font/roboto128.fnt"));
        }

        textureAtlas = new TextureAtlas(Gdx.files.internal("Atlas/MainAtlas.atlas"));
        platRegion = textureAtlas.findRegion("PlatTile");
        wallRegion = textureAtlas.findRegion("WallTile");
        arrowRegion = textureAtlas.findRegion("BlackArrow");
        playerRegion = textureAtlas.findRegion("Ball");
        barEffectRegion = textureAtlas.findRegion("BarEffectBlock");

        // UI
        playButtonRegion = textureAtlas.findRegion("Play");
        restartButtonRegion = textureAtlas.findRegion("Restart");
        infoButtonRegion = textureAtlas.findRegion("Info");
        infoPlusRegion = textureAtlas.findRegion("InfoPlus");
        tapRegion = textureAtlas.findRegion("Tap");
        leaderboardRegion = textureAtlas.findRegion("Leaderboard");

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

    private BitmapFont generateFontASyn(int userSize){
        // generating a font synchronously
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("GenFont/Roboto-LightItalic.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = userSize;
        parameter.incremental = true; // fixes rendering issues at diff sizes
        parameter.color = Color.WHITE;
        BitmapFont font = generator.generateFont(parameter);
        generator.dispose();
        return font;
    }
}