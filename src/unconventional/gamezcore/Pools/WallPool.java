package unconventional.gamezcore.Pools;

import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Pool;

import unconventional.gamezcore.objects.Wall;
import unconventional.gamezcore.handlers.Cons;

/**
 * Created by Rafael on 1/1/2017.
 *
 */
public class WallPool extends Pool<Wall> {

    private World world;
    public int newCount = 0;

    public WallPool(World world){
        this.world = world;
    }

    @Override
    protected Wall newObject() {
        newCount++;
        return new Wall(world, Cons.WALL_WIDTH, Cons.WALL_HEIGHT, 0, 0);
    }
}
