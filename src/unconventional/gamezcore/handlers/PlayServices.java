package unconventional.gamezcore.handlers;

/**
 * Created by Rafael on 1/27/2017.
 *
 */

public interface PlayServices {
    public void signIn();
    public void signOut();
    public void rateGame();
    public void unlockAchievement(String achievementId);
    public void submitScore(int highScore);
    public void showAchievement();
    public void showScore();
    public boolean isSignedIn();
}
