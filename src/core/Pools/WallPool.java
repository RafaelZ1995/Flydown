package core.Pools;

import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Pool;

import core.objects.Wall;

import static core.handlers.Cons.WALL_HEIGHT;
import static core.handlers.Cons.WALL_WIDTH;

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
        return new Wall(world, WALL_WIDTH, WALL_HEIGHT, 0, 0);
    }
}
