package core.Pools;

import com.badlogic.gdx.utils.Pool;

import core.particleeffects.BarEffect;
import core.screens.PlayScreen;

/**
 * Created by Rafael on 1/6/2017.
 *
 */
public class BarEffectPool extends Pool<BarEffect> {

    PlayScreen playScreen;

    public BarEffectPool(PlayScreen playScreen){
        this.playScreen = playScreen;
    }

    @Override
    protected BarEffect newObject() {
        return new BarEffect(playScreen);
    }
}
