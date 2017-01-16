package core.Pools;

import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Pool;

import core.pickups.ScorePickup;

/**
 * Created by Rafael on 1/2/2017.
 *
 */
public class ScorePickupPool extends Pool<ScorePickup> {

    private World world;

    public ScorePickupPool(World world){
        this.world = world;
    }

    @Override
    protected ScorePickup newObject() {
        return new ScorePickup(world, 0, 0);
    }
}
