package unconventional.gamez.game;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.GameHelper;

import unconventional.gamezcore.handlers.PlayServices;

public class AndroidLauncher extends AndroidApplication implements PlayServices{

	private GameHelper gameHelper; // basegameutils
	private final static int requestCode = 1;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new unconventional.gamezcore.game.GameApp(this), config);


		gameHelper = new GameHelper(this, GameHelper.CLIENT_GAMES);
		gameHelper.enableDebugLog(false);

		GameHelper.GameHelperListener gameHelperListener = new GameHelper.GameHelperListener()
		{
			@Override
			public void onSignInFailed(){ }

			@Override
			public void onSignInSucceeded(){ }
		};

		gameHelper.setup(gameHelperListener);
	}

	@Override
	protected void onStart()
	{
		super.onStart();
		gameHelper.onStart(this);
	}

	@Override
	protected void onStop()
	{
		super.onStop();
		gameHelper.onStop();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		gameHelper.onActivityResult(requestCode, resultCode, data);
	}


	// Play Services Interface

	@Override
	public void signIn()
	{
		try
		{
			runOnUiThread(new Runnable()
			{
				@Override
				public void run()
				{
					gameHelper.beginUserInitiatedSignIn();
				}
			});
		}
		catch (Exception e)
		{
			Gdx.app.log("MainActivity", "Log in failed: " + e.getMessage() + ".");
		}
	}

	@Override
	public void signOut()
	{
		try
		{
			runOnUiThread(new Runnable()
			{
				@Override
				public void run()
				{
					gameHelper.signOut();
				}
			});
		}
		catch (Exception e)
		{
			Gdx.app.log("MainActivity", "Log out failed: " + e.getMessage() + ".");
		}
	}

	// needs editing
	@Override
	public void rateGame()
	{
		String str = "Your PlayStore Link";
		startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(str)));
	}

	@Override
	public void unlockAchievement(String achievementId)
	{
		String temp = null;
		if (achievementId.equals(R.string.achievement_10_orbs))
			temp = getString(R.string.achievement_10_orbs);
		else if (achievementId.equals(R.string.achievement_20_orbs))
			temp = getString(R.string.achievement_20_orbs);
		else if (achievementId.equals(R.string.achievement_30_orbs_getting_the_hang_of_it))
			temp = getString(R.string.achievement_30_orbs_getting_the_hang_of_it);
		else if (achievementId.equals(R.string.achievement_50_orbs_starting_to_feel_tight))
			temp = getString(R.string.achievement_50_orbs_starting_to_feel_tight);
		else if (achievementId.equals(R.string.achievement_77_orbs_as_far_as_ive_gotten))
			temp = getString(R.string.achievement_77_orbs_as_far_as_ive_gotten);
		else if (achievementId.equals(R.string.achievement_100_orbs_will_you_reign_the_leaderboards))
			temp = getString(R.string.achievement_100_orbs_will_you_reign_the_leaderboards);

		if (temp == null){
			System.out.println("@@@ Error @@@, achievement Id given in parameter does not match any of the achievement Ids");
			System.out.println("not unlocked");
		} else {
			Games.Achievements.unlock(gameHelper.getApiClient(), temp);
			System.out.println("unlocked");
		}

	}

	@Override
	public void submitScore(int highScore)
	{
		if (isSignedIn() == true)
		{
			Games.Leaderboards.submitScore(gameHelper.getApiClient(),
					getString(R.string.leaderboard_how_deep_can_you_go), highScore);
		}
	}

	@Override
	public void showAchievement()
	{
		if (isSignedIn() == true)
		{


			/*
			startActivityForResult(
					Games.Achievements.getAchievementsIntent(
							gameHelper.getApiClient(),
							getString(R.string.achievement_10_orbs)),
					requestCode);
					*/
		}
		else
		{
			signIn();
		}
	}

	@Override
	public void showScore()
	{
		if (isSignedIn() == true)
		{
			startActivityForResult(Games.Leaderboards.getLeaderboardIntent(gameHelper.getApiClient(),
					getString(R.string.leaderboard_how_deep_can_you_go)), requestCode);
		}
		else
		{
			signIn();
		}
	}

	@Override
	public boolean isSignedIn()
	{
		return gameHelper.isSignedIn();
	}
}
