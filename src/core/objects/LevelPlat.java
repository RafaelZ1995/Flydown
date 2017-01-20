package core.objects;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.World;

import core.Pools.PlatPool;

import static core.handlers.Cons.PLAT_HEIGHT;
import static core.handlers.Cons.VIR_WIDTH;
import static core.handlers.Cons.BALL_DIAM;

/**
 * Created by Rafae on 1/17/2017.
 */
public class LevelPlat {
    private Plat leftPlat;
    private Plat rightPlat;

    public LevelPlat(World world, PlatPool platPool, float y) {
        float gapWidth = MathUtils.random((float) (BALL_DIAM * 1.5), (float) VIR_WIDTH / 2);
        float leftWidth = MathUtils.random(VIR_WIDTH * 0.1f,  VIR_WIDTH - gapWidth - VIR_WIDTH * 0.1f);

        // Get CURRENT PLAT
        Plat leftPlat = platPool.obtain();
        leftPlat.setBodyPosition(0, y);
        leftPlat.setSize(leftWidth, PLAT_HEIGHT);
        //                   r,   -(overall depth, top of screen) - ( depth between each plat)

        Plat rightPlat = platPool.obtain();
        rightPlat.setBodyPosition(leftPlat.width + gapWidth, y);
        rightPlat.setSize(VIR_WIDTH - leftWidth - gapWidth, PLAT_HEIGHT);

    }
}
