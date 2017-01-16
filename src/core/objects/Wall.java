package core.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.World;

import core.game.GameApp;
import core.handlers.Cons;
import core.handlers.Res;

import static core.handlers.Cons.BLUE;
import static core.handlers.Cons.PPM;

/**
 * Created by Rafael on 1/1/2017.
 *
 */
public class Wall extends Box2dPlat {

    public Wall(World world, int width, int height, float initVirX, float initVirY) {
        super(world, width, height, initVirX, initVirY);
        body.getFixtureList().first().setUserData("Wall");

        sprite = new Sprite(Res.wallRegion);//new Sprite(Res.platTexture, width, height);
        sprite.setColor(GameApp.CurrentThemeColor);
        sprite.setSize(width, height);
        sprite.setOrigin(width / 2, height / 2); // needed for sprite to stay on body when rotating
    }

    public void update() {
        virX = body.getPosition().x * PPM - width / 2; // revert the additions done when setting position of the body in construct2d()
        virY = body.getPosition().y * PPM + height / 2; //  revert the substraction done when setting position of the body in construct2d()
        sprite.setColor(GameApp.CurrentThemeColor);
        sprite.setPosition(virX, virY - height);
        sprite.setRotation((float) Math.toDegrees(body.getAngle())); // so that sprite is attached to body
    }

    public void render() {
        update();
        sprite.draw(sb);
    }


    @Override
    public void reset() {

    }
}
