package unconventional.gamezcore.Pools;

import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by Rafael on 1/2/2017.
 *
 */
public class ScorePickupPool extends Pool<unconventional.gamezcore.pickups.ScorePickup> {

    private World world;

    public ScorePickupPool(World world){
        this.world = world;
    }

    @Override
    protected unconventional.gamezcore.pickups.ScorePickup newObject() {
        return new unconventional.gamezcore.pickups.ScorePickup(world, 0, 0);
    }
}
