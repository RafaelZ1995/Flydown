package core.Pools;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Pool;

import core.objects.Plat;

import static core.handlers.Cons.PLAT_WIDTH;
import static core.handlers.Cons.PLAT_HEIGHT;

/**
 * Created by Rafael on 1/1/2017.
 *
 */
public class PlatPool extends Pool<Plat> {

    private World world;

    public PlatPool(World world){
        this.world = world;
    }

    @Override
    protected Plat newObject() {
        Plat newPlat = new Plat(world, PLAT_WIDTH, PLAT_HEIGHT, 0, 0);
        return newPlat;
    }
}
