package core.Pools;

import com.badlogic.gdx.utils.Pool;

import core.particleeffects.BallExplosion;

/**
 * Created by Rafael on 1/11/2017.
 *
 */
public class BallExplosionPool extends Pool<BallExplosion> {

    @Override
    protected BallExplosion newObject() {
        return new BallExplosion();
    }
}
