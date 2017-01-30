package unconventional.gamezcore.handlers;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import unconventional.gamezcore.objects.Ball;


/**
 * Created by Rafael on 12/28/2016.
 *
 */
public class PlayContactListener implements ContactListener{

    private unconventional.gamezcore.screens.PlayScreen playScreen;

    public PlayContactListener(unconventional.gamezcore.screens.PlayScreen playScreen) {
        this.playScreen = playScreen;
    }

    @Override
    public void beginContact(Contact c) {

        // extract colliding features from c
        Fixture fa = c.getFixtureA();
        Fixture fb = c.getFixtureB();

        // System.out.println("fa: " + fa.getUserData() + " fb: " + fb.getUserData());
        // either fixture might be null sometimes.
        if (fa == null || fb == null) {
            return;
        }
        if (fa.getUserData() == null || fb.getUserData() == null){
            return;
        }

        handle_ball_plat_collision(fa, fb);
        handle_ball_scorePickup_collision(fa, fb);

    }


    // -------------------------------------- PRIVATE METHODS --------------------------------------
    private void handle_ball_plat_collision(Fixture fa, Fixture fb){
        if (fa.getUserData() instanceof Ball || fb.getUserData() instanceof Ball) {
            if (fa.getUserData().equals("Platform") || fb.getUserData().equals("Platform")) {

                Ball ball = null;
                if (fa.getFilterData().categoryBits == Cons.BIT_PLAYER)
                    ball = (Ball) fa.getUserData();
                else if (fb.getFilterData().categoryBits == Cons.BIT_PLAYER)
                    ball = (Ball) fb.getUserData();

                ball.setCrashed(true);
                playScreen.renderBallExplosion(ball.getVirX(), ball.getVirY());
            }
        }
    }

    /**
     * Increase Score when Ball picks up a Scorepickup
     * NOTE: that once collected, the box2d body actually stays there. it gets repositioned in renderAndFreeScorePickups() in PlayScreen
     * @param fa
     * @param fb
     */
    private void handle_ball_scorePickup_collision(Fixture fa, Fixture fb) {
        if (fa.getUserData() instanceof Ball || fb.getUserData() instanceof Ball) {
            if (fa.getUserData() instanceof unconventional.gamezcore.pickups.ScorePickup || fb.getUserData() instanceof unconventional.gamezcore.pickups.ScorePickup) {
                playScreen.currentScore++; // increase score
                playScreen.getPlayer().resetExtraRadiusGrowth();

                // free this scorePickup
                unconventional.gamezcore.pickups.ScorePickup sp = null;
                if (fa.getFilterData().categoryBits == Cons.BIT_SCOREPICKUP)
                    sp = (unconventional.gamezcore.pickups.ScorePickup) fa.getUserData();
                else if (fb.getFilterData().categoryBits == Cons.BIT_SCOREPICKUP)
                    sp = (unconventional.gamezcore.pickups.ScorePickup) fb.getUserData();


                // set bar effect
                playScreen.setBarEffect(sp.getX(), Cons.BALL_POS_ON_HUD);
                // free This score pickup
                playScreen.setToRemoveScorePickup(sp);
            }
        }
    }

    @Override
    public void endContact(Contact c) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
