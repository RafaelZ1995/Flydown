package unconventional.gamezcore.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import unconventional.gamezcore.game.GameApp;
import unconventional.gamezcore.handlers.Res;
import unconventional.gamezcore.handlers.Cons;

/**
 * Created by Rafael on 1/12/2017.
 *
 */
public class ScoreStage {

    private Stage stage;
    public unconventional.gamezcore.screens.PlayScreen playScreen;
    private SpriteBatch sb;

    // permanent texts
    private final PermanentText overPText = new PermanentText("GAME OVER", Cons.VIR_WIDTH * 0.5f, Cons.VIR_HEIGHT * 0.90f, "128");
    private final PermanentText scoreText = new PermanentText("score", Cons.VIR_WIDTH * 0.25f, Cons.VIR_HEIGHT * 0.65f, "64");
    private final PermanentText bestText = new PermanentText("best", Cons.VIR_WIDTH * 0.75f, Cons.VIR_HEIGHT * 0.65f, "64");

    // current score
    private float scoreX = Cons.VIR_WIDTH * 0.25f - Res.userFontSizeSmall;
    private float scoreY = Cons.VIR_HEIGHT * 0.70f;

    // Best Score
    private float bestScoreX = Cons.VIR_WIDTH * 0.75f - Res.userFontSizeSmall;
    private float bestScoreY = Cons.VIR_HEIGHT * 0.70f;

    // fade background
    private Sprite bgSprite;
    private float bgAlpha;

    // lets use some Math to animate sizes!
    private Button restartButton;
    private double dt;
    private float restartButtonExtraSize = 0;

    // actual best score yet (int)
    private String bestScore;
    private String scoreStr;

    public ScoreStage(final unconventional.gamezcore.screens.PlayScreen playScreen) {
        this.playScreen = playScreen;
        sb = GameApp.APP.getSb();

        // fade
        bgSprite = new Sprite(Res.barEffectRegion);
        bgSprite.setSize(Cons.VIR_WIDTH, Cons.VIR_HEIGHT);
        bgSprite.setColor(Color.BLACK);
        bgSprite.setAlpha(0);

        // PSEUDO SCORE SCREEN
        stage = new Stage();
        Drawable drawable = new TextureRegionDrawable(new TextureRegion(Res.restartButtonRegion));
        restartButton = new Button(drawable);

        restartButton.setHeight(Cons.VIR_HEIGHT / 5);
        restartButton.setWidth(Cons.VIR_WIDTH / 3);
        restartButton.setX(Cons.VIR_WIDTH / 2 - restartButton.getWidth() / 2);
        restartButton.setY(Cons.VIR_HEIGHT / 3 - restartButton.getHeight() / 2);
        restartButton.setColor(GameApp.CurrentThemeColor);
        stage.addActor(restartButton);


        restartButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                unconventional.gamezcore.screens.PlayScreen ps = new unconventional.gamezcore.screens.PlayScreen();
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
        restartButton.setWidth(Cons.VIR_WIDTH / 3 + restartButtonExtraSize);
        restartButton.setHeight(Cons.VIR_HEIGHT / 5 + restartButtonExtraSize);
        restartButton.setX(Cons.VIR_WIDTH / 2 - restartButton.getWidth() / 2);
        restartButton.setY(Cons.VIR_HEIGHT / 3 - restartButton.getHeight() / 2);
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
        overPText.render();
        scoreText.render();
        bestText.render();

        // render score
        if (playScreen.getScore() > 9)
            scoreX = Cons.VIR_WIDTH * 0.25f - Res.userFontSizeSmall;
        scoreStr = String.valueOf(playScreen.getScore());
        Res.font128.draw(sb, scoreStr, scoreX, scoreY);

        // render best score
        if (Res.prefs.getInteger("bestscore") > 9)
            bestScoreX = Cons.VIR_WIDTH * 0.75f - Res.userFontSizeSmall;
        bestScore = String.valueOf(Res.prefs.getInteger("bestscore"));
        Res.font128.draw(sb, bestScore, bestScoreX, bestScoreY);


    }
}
