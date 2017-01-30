package unconventional.gamezcore.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

/**
 * Created by Rafael on 10/4/2016.
 *
 */
public class Cons {

    // Options
    public static boolean FREE_ROAM = true; // freeroam doesnt work with growthPoisoned

    // box2d works better with fixed time steps
    public static final float STEP = 1 / 60f;

    // Scale for ortho cameras
    public static final int SCALE = 1;

    // Width and Height but times the SCALE
    public static final int VIR_WIDTH = Gdx.graphics.getWidth() * SCALE;
    public static final int VIR_HEIGHT = Gdx.graphics.getHeight() * SCALE;

    // pixels per meter
    public static final float PPM = 100;

    // categorybits
    public static final short BIT_PLAYER = 2;
    public static final short BIT_PLAT = 4;
    public static final short BIT_SCOREPICKUP = 8;

    // Player Diameter
    public static final int BALL_DIAM = (VIR_WIDTH / 30)*3;

    // set Ball's Fall speed in terms of the device's Height!
    public static final float TIME_TO_TRAVEL_1_DEPTH = 7000; // 6 seconds
    public static final float BALL_FALL_SPEED = -(VIR_HEIGHT / TIME_TO_TRAVEL_1_DEPTH)*10;
    public static final float BALL_X_FORCE = ((float) VIR_WIDTH / 1500) * 500;


    // Map info
    public final static int PLATS_PER_DEPTH = 3;


    // Plat info
    public static final int PLAT_WIDTH = VIR_WIDTH / 3;
    public static final int PLAT_HEIGHT = VIR_HEIGHT / 30;

    // Wall info
    public static final int WALL_WIDTH = VIR_WIDTH / 40;
    public static final int WALL_HEIGHT = VIR_HEIGHT;
    public static final int LEFT_WALL_X = 0;// - WALL_WIDTH; // ADD HALF OF WIDTH if you want to show
    public static final int RIGHT_WALL_X = VIR_WIDTH - WALL_WIDTH ; // ADD HALF OF WIDTH

    // ScorePickup

    // Hud info
    public static final float BALL_POS_ON_HUD = VIR_HEIGHT * 0.70f;


    // Color Pallete
    public static final Color RED = new Color(255/255f,0,0,1); // platforms
    public static final Color BLUE = new Color(18/255f,1/255f,189/255f,1);
    public static final Color YELLOW = new Color(216/255f,168/255f,0/255f,1);

}