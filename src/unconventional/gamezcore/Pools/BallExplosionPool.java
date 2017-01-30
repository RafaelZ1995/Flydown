package unconventional.gamezcore.Pools;

import com.badlogic.gdx.utils.Pool;

/**
 * Created by Rafael on 1/11/2017.
 *
 */
public class BallExplosionPool extends Pool<unconventional.gamezcore.particleeffects.BallExplosion> {

    @Override
    protected unconventional.gamezcore.particleeffects.BallExplosion newObject() {
        return new unconventional.gamezcore.particleeffects.BallExplosion();
    }
}
