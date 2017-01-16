package core.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import core.game.GameApp;
import core.handlers.Res;

import static core.handlers.Cons.VIR_HEIGHT;
import static core.handlers.Cons.VIR_WIDTH;

/**
 * Created by Rafael on 1/12/2017.
 *
 */
public class ScoreStage {

    private Stage stage;
    public PlayScreen playScreen;
    private SpriteBatch sb;

    // permanent texts
    private final PermanentText overPText = new PermanentText("GAME OVER", VIR_WIDTH * 0.5f, VIR_HEIGHT * 0.90f);
    private final PermanentText scoreText = new PermanentText("score", VIR_WIDTH * 0.25f, VIR_HEIGHT * 0.65f);
    private final PermanentText bestText = new PermanentText("best", VIR_WIDTH * 0.75f, VIR_HEIGHT * 0.65f);

    // current score
    private final float scoreX = VIR_WIDTH * 0.25f - 128;
    private final float scoreY = VIR_HEIGHT * 0.70f;

    // Best Score
    private final float bestScoreX = VIR_WIDTH * 0.75f - 128;
    private final float bestScoreY = VIR_HEIGHT * 0.70f;

    // fade background
    private Sprite bgSprite;
    private float bgAlpha;

    // lets use some Math to animate sizes!
    private Button restartButton;
    private double dt;
    private float restartButtonExtraSize = 0;

    public ScoreStage(final PlayScreen playScreen) {
        this.playScreen = playScreen;
        sb = GameApp.APP.getSb();

        // fade
        bgSprite = new Sprite(Res.barEffectRegion);
        bgSprite.setSize(VIR_WIDTH, VIR_HEIGHT);
        bgSprite.setColor(Color.BLACK);
        bgSprite.setAlpha(0);

        // PSEUDO SCORE SCREEN
        stage = new Stage();
        Drawable drawable = new TextureRegionDrawable(new TextureRegion(Res.restartButtonRegion));
        restartButton = new Button(drawable);

        restartButton.setHeight(VIR_HEIGHT / 5);
        restartButton.setWidth(VIR_WIDTH / 3);
        restartButton.setX(VIR_WIDTH / 2 - restartButton.getWidth() / 2);
        restartButton.setY(VIR_HEIGHT / 3 - restartButton.getHeight() / 2);
        restartButton.setColor(GameApp.CurrentThemeColor);
        stage.addActor(restartButton);


        restartButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                PlayScreen ps = new PlayScreen();
                playScreen.dispose();
                GameApp.APP.setScreen(ps);
                return true;
            }
        });
    }

    public Stage getStage() {
        return stage;
    }

    private void update() {


        // restart button
        dt += 0.02f;
        restartButtonExtraSize = (float) Math.sin(dt) * 70;
        restartButton.setWidth(VIR_WIDTH / 3 + restartButtonExtraSize);
        restartButton.setHeight(VIR_HEIGHT / 5 + restartButtonExtraSize);
        restartButton.setX(VIR_WIDTH / 2 - restartButton.getWidth() / 2);
        restartButton.setY(VIR_HEIGHT / 3 - restartButton.getHeight() / 2);
        restartButton.setColor(GameApp.CurrentThemeColor);

        // fade in
        bgAlpha += 0.01f;
        if (bgAlpha > 1)
            bgAlpha = 1;
        bgSprite.setAlpha(bgAlpha);
    }

    public void render() {
        update();

        // back ground
        bgSprite.draw(sb);

        // render permanent texts
        Res.font128.getData().setScale(1f, 1f);
        overPText.render();
        Res.font128.getData().setScale(0.4f);
        scoreText.render();
        bestText.render();
        Res.font128.getData().setScale(1f, 1f);

        // render score
        String scoreStr = String.valueOf(playScreen.getScore());
        Res.font128.draw(sb, scoreStr, scoreX, scoreY);

        // render best score
        String bestScore = String.valueOf(Res.prefs.getInteger("bestscore"));
        Res.font128.draw(sb, bestScore, bestScoreX, bestScoreY);


    }
}
