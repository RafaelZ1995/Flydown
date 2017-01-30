package unconventional.gamezcore.objects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.World;

import unconventional.gamezcore.game.GameApp;

/**
 * Created by Rafael on 1/1/2017.
 *
 */
public class Wall extends Box2dPlat {

    public Wall(World world, int width, int height, float initVirX, float initVirY) {
        super(world, width, height, initVirX, initVirY);
        body.getFixtureList().first().setUserData("Wall");

        sprite = new Sprite(unconventional.gamezcore.handlers.Res.wallRegion);//new Sprite(Res.platTexture, width, height);
        sprite.setColor(GameApp.CurrentThemeColor);
        sprite.setSize(width, height);
        sprite.setOrigin(width / 2, height / 2); // needed for sprite to stay on body when rotating
    }

    public void update() {
        virX = body.getPosition().x * unconventional.gamezcore.handlers.Cons.PPM - width / 2; // revert the additions done when setting position of the body in construct2d()
        virY = body.getPosition().y * unconventional.gamezcore.handlers.Cons.PPM + height / 2; //  revert the substraction done when setting position of the body in construct2d()
        sprite.setColor(GameApp.CurrentThemeColor);
        sprite.setPosition(virX, virY - height);
        sprite.setRotation((float) Math.toDegrees(body.getAngle())); // so that sprite is attached to body
    }

    public void render() {
        update();
        sprite.draw(sb);
    }

    @Override
    public void setSize(float width, float height) {

    }


    @Override
    public void reset() {

    }
}
