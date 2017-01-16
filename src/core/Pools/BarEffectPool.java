package core.Pools;

import com.badlogic.gdx.utils.Pool;

import core.particleeffects.BarEffect;

/**
 * Created by Rafael on 1/6/2017.
 *
 */
public class BarEffectPool extends Pool<BarEffect> {

    @Override
    protected BarEffect newObject() {
        return new BarEffect();
    }
}
