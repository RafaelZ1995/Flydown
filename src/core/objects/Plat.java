package core.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import core.game.GameApp;
import core.handlers.Cons;
import core.handlers.Res;

import static core.handlers.Cons.PLAT_HEIGHT;
import static core.handlers.Cons.PLAT_WIDTH;
import static core.handlers.Cons.PPM;

/**
 * Created by Rafael on 1/1/2017.
 *
 */
public class Plat extends Box2dPlat {

    private ParticleEffect effect;


    public Plat(World world, int width, int height, float initVirX, float initVirY){
        super(world, width, height, initVirX, initVirY);
        body.getFixtureList().first().setUserData("Platform");
        sprite = new Sprite(Res.platRegion);
        sprite.setColor(GameApp.CurrentThemeColor);
        sprite.setSize(width, height);
        sprite.setOrigin(width / 2, height / 2); // needed for sprite to stay on body when rotating
        initParticleEffect();
    }

    void initParticleEffect() {
        effect = new ParticleEffect();
        effect.load(Gdx.files.internal("Particles/platEffect.p"), Res.textureAtlas);
        effect.getEmitters().first().setPosition(virX, virY);
        effect.getEmitters().first().getScale().setHigh(width);
        effect.getEmitters().first().getTint().setColors(new float[]{1f, 1f, 1f});
        //effect.getEmitters().first().getRotation().setHigh(body.getAngle());
        //effect.scaleEffect(2);
        effect.start();
    }

    private void updateEffect() {
        if (effect.isComplete())
            effect.reset();
        effect.setPosition(virX + width / 2, virY - height);
        effect.update(Gdx.graphics.getDeltaTime());
        effect.getEmitters().first().getRotation().setHigh(sprite.getRotation()); // for rotation to work, the rotation button in the particle2d tool has to be on
    }

    public void update() {
        updateEffect();
        virX = body.getPosition().x * PPM - width / 2; // revert the additions done when setting position of the body in construct2d()
        virY = body.getPosition().y * PPM + height / 2; //  revert the substraction done when setting position of the body in construct2d()
        sprite.setColor(GameApp.CurrentThemeColor);
        sprite.setPosition(virX, virY - height);
        sprite.setRotation((float) Math.toDegrees(body.getAngle())); // so that sprite is attached to body
    }

    public void render() {
        update();
        sprite.draw(sb);
        //effect.draw(sb, Gdx.graphics.getDeltaTime());
    }

    @Override
    public void setSize(float width, float height) {

        // body
        PolygonShape s = (PolygonShape) body.getFixtureList().first().getShape();
        this.width = width;
        this.height = height;
        s.setAsBox(width / 2 / PPM, height / 2 / PPM);

        // sprite
        sprite.setSize(width, height);
        sprite.setOrigin(width / 2, height / 2);

        // effects
        effect.getEmitters().first().getScale().setHigh(width);
    }


    @Override
    public void reset() {
        body.setAngularVelocity(0);
    }
}
