package unconventional.gamezcore.objects;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.World;

import unconventional.gamezcore.handlers.Cons;

/**
 * Created by Rafae on 1/17/2017.
 */
public class LevelPlat {
    private Plat leftPlat;
    private Plat rightPlat;

    public LevelPlat(World world, unconventional.gamezcore.Pools.PlatPool platPool, float y) {
        float gapWidth = MathUtils.random((float) (Cons.BALL_DIAM * 1.5), (float) Cons.VIR_WIDTH / 2);
        float leftWidth = MathUtils.random(Cons.VIR_WIDTH * 0.1f,  Cons.VIR_WIDTH - gapWidth - Cons.VIR_WIDTH * 0.1f);

        // Get CURRENT PLAT
        Plat leftPlat = platPool.obtain();
        leftPlat.setBodyPosition(0, y);
        leftPlat.setSize(leftWidth, Cons.PLAT_HEIGHT);
        //                   r,   -(overall depth, top of screen) - ( depth between each plat)

        Plat rightPlat = platPool.obtain();
        rightPlat.setBodyPosition(leftPlat.width + gapWidth, y);
        rightPlat.setSize(Cons.VIR_WIDTH - leftWidth - gapWidth, Cons.PLAT_HEIGHT);

    }
}
