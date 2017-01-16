package core.handlers;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import core.game.GameApp;
import core.objects.Ball;
import core.pickups.ScorePickup;
import core.screens.MainScreen;
import core.screens.PlayScreen;


/**
 * Created by Rafael on 12/28/2016.
 *
 */
public class MainScreenCL implements ContactListener{

    public MainScreenCL() {
    }

    @Override
    public void beginContact(Contact c) {

        // extract colliding features from c
        Fixture fa = c.getFixtureA();
        Fixture fb = c.getFixtureB();

        // either fixture might be null sometimes.
        if (fa == null || fb == null) {
            return;
        }
        if (fa.getUserData() == null || fb.getUserData() == null){
            return;
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
