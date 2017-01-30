package unconventional.gamezcore.Pools;

import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Pool;

import static unconventional.gamezcore.handlers.Cons.PLAT_WIDTH;
import static unconventional.gamezcore.handlers.Cons.PLAT_HEIGHT;

/**
 * Created by Rafael on 1/1/2017.
 *
 */
public class PlatPool extends Pool<unconventional.gamezcore.objects.Plat> {

    private World world;

    public PlatPool(World world){
        this.world = world;
    }

    @Override
    protected unconventional.gamezcore.objects.Plat newObject() {
        unconventional.gamezcore.objects.Plat newPlat = new unconventional.gamezcore.objects.Plat(world, PLAT_WIDTH, PLAT_HEIGHT, 0, 0);
        return newPlat;
    }
}
