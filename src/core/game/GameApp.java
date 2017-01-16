package core.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import core.handlers.Res;
import core.screens.MainScreen;
import core.screens.SplashScreen;

import static core.handlers.Cons.VIR_HEIGHT;
import static core.handlers.Cons.VIR_WIDTH;


/**
 * IMPORTANT:
 * SHOULDNT THIS CLASS BE THE ONE TO COMPLETELY HANDLE WHAT SCREEN IS PLAYING?
 * INSTEAD OF MAINSCREEN BEING THE making a new PlayScreen()
 * BUT THEN AGAIN I DEFEATED THE PURPOSE OF THAT BY MAKING THE GameApp instance static in Cons
 * I think this should still be the one doing it though. just for organization.
 */
public class GameApp extends Game {



    // fading in
    private Stage stage;
    private Table fadingTable;
    private Pixmap pixmap;
    private Drawable drawable;

    // my fade in
    private Sprite sprite;
    //private Texture texture;
    private float currentFade = 0;
    private boolean shouldFadeOnce = false;
    private boolean fadeInDone = false;
    private float fadeFor = 0.5f; // stay black for half a second
    private float hasFadedFor = 0; // amount of time it has been black


    // public
    public static GameApp APP; // Tried making this a final static in Cons... That is a big NO NO.
    // title still doesnt change color with it.
    public static Color CurrentThemeColor = Color.WHITE;

    public boolean isBackPressed = false;
    private boolean isMainScreenSet = false; // after splash screen is done

    // private
    private SpriteBatch sb;
    private OrthographicCamera cam;
    private Res res; // this does need to be initialized
    private OrthographicCamera fontcam;
    private boolean isSplashDone;
    private float splashTime = 0;

    FPSLogger logger = new FPSLogger();
    private boolean fadeOutDone;


    @Override
    public void create() {
        APP = this;
        sb = new SpriteBatch();
        Gdx.input.setCatchBackKey(true);

        // Load assets
        res = new Res();

        // Main Player Cam
        cam = new OrthographicCamera();
        cam.setToOrtho(false, VIR_WIDTH, VIR_HEIGHT);

        // Font Cam
        fontcam = new OrthographicCamera();
        fontcam.setToOrtho(false, VIR_WIDTH, VIR_HEIGHT);

        // initialize main screen
        this.setScreen(new SplashScreen());


        // fading in
        stage = new Stage();
        fadingTable = new Table();
        //pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);

        // my fade in
        //texture = new Texture(Gdx.files.internal(Res.wallRegion));
        sprite = new Sprite(Res.wallRegion);
        sprite.setSize(VIR_WIDTH, VIR_HEIGHT);
        sprite.setColor(Color.BLACK);

        System.out.println("Vir_WIDTH: " + VIR_WIDTH + "  Vir_HEIGHT: " + VIR_HEIGHT);
    }

    @Override
    public void render() {
        //System.out.println("FPS: " + Gdx.graphics.getFramesPerSecond());
        //logger.log();
        //System.out.println("dt: " + Gdx.graphics.getDeltaTime() * 10);

        sb.totalRenderCalls = 0;

        if (splashTime > 1.5)
            shouldFadeOnce = true;

        if (splashTime > 2){ // if true, then we are done loading

            if (!isMainScreenSet) {
                APP.getScreen().dispose();
                isMainScreenSet = true;
                APP.setScreen(new MainScreen());
            }
            super.render();
        }else{
            super.render();
            splashTime += Gdx.graphics.getDeltaTime();
        }

        int calls = sb.totalRenderCalls;
        //System.out.println("total calls: " + calls);
        if (shouldFadeOnce)
            renderFading();
    }

    private void renderFading() {

        if (!fadeInDone)
            currentFade += 0.05f;

        if (currentFade > 1) {
            fadeInDone = true;
        }

        if (fadeInDone){
            hasFadedFor += Gdx.graphics.getDeltaTime();
        }

        if (fadeInDone && hasFadedFor > fadeFor) {


            currentFade -= 0.05f;
            if (currentFade < 0){
                currentFade = 0;
                fadeOutDone = true;
            }
        }

        if (fadeInDone && fadeOutDone){
            shouldFadeOnce = false;
            return;
        }



        sprite.setAlpha(currentFade);
        //System.out.println(currentFade);
        sb.begin();
        sb.setProjectionMatrix(fontcam.combined);
        sprite.draw(sb);
        sb.end();
    }

    @Override
    public void dispose() {
        sb.dispose();
        res.dispose();
    }


    public SpriteBatch getSb() {
        return sb;
    }

    public OrthographicCamera getCam() {
        return cam;
    }

    public OrthographicCamera getFontcam() {
        return fontcam;
    }


}
