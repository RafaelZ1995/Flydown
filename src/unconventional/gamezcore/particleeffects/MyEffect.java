package unconventional.gamezcore.particleeffects;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Pool;

import unconventional.gamezcore.game.GameApp;

/**
 * Created by Rafael on 1/6/2017.
 *
 */
public abstract class MyEffect implements Pool.Poolable{

    protected ParticleEffect effect;
    protected SpriteBatch sb;


    public MyEffect() {
        sb = GameApp.APP.getSb();
    }

    abstract void render();

    abstract void dispose();
}
