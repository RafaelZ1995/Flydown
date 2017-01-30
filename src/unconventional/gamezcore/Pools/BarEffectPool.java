package unconventional.gamezcore.Pools;

import com.badlogic.gdx.utils.Pool;

import unconventional.gamezcore.screens.PlayScreen;

/**
 * Created by Rafael on 1/6/2017.
 *
 */
public class BarEffectPool extends Pool<unconventional.gamezcore.particleeffects.BarEffect> {

    PlayScreen playScreen;

    public BarEffectPool(PlayScreen playScreen){
        this.playScreen = playScreen;
    }

    @Override
    protected unconventional.gamezcore.particleeffects.BarEffect newObject() {
        return new unconventional.gamezcore.particleeffects.BarEffect(playScreen);
    }
}
