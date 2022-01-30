package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.infoClasses.LevelInfo;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class MainMenu implements Screen {

	final MyGdxGame game;
	LevelInfo[] level_data;
	public static Texture backgroundTexture;
    public static Sprite backgroundSprite;

	private String endSplash;
    private BitmapFont font;

	OrthographicCamera camera;

	public MainMenu(final MyGdxGame game, LevelInfo[] level_data) {
		this.game = game;
		this.level_data = level_data;

		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1920, 1080);

		backgroundTexture = new Texture("AwesomeSpatialEccentricCtrustacean-PuzzleTitleScreenPogChampV2.png");
        backgroundSprite = new Sprite(backgroundTexture);
		//backgroundSprite.setBounds(0, 0, 1f, 1f);
        //backgroundSprite.setOriginCenter();
		font = new BitmapFont();
		endSplash = "";
	}

	private String getDeathSplash() {
		String[] lose = new String[45];
		int index = (int)(44.0 * Math.random());

		lose[0] = "i Cri Errytim";
		lose[1] = "El PSy Kongroo";
		lose[2] = "You Died.";
		lose[3] = "Game Over";
		lose[4] = "Stay DETERMINED";
		lose[5] = "Gotta die sometime";
		lose[6] = "You lost";
		lose[7] = "If you die in the game you die for real";
		lose[8] = "That's it folks";
		lose[9] = "rip in peace";
		lose[10] = "smh in my head";
		lose[11] = "Press F to pay respects";
		lose[12] = "Play again pls";
		lose[13] = "Subscribe to feed my dying cats";
		lose[14] = "F";
		lose[15] = "My bank pin is 1234";
		lose[16] = "Climb to challenger";
		lose[15] = "It was me, Dio!";
		lose[16] = "This must be the work of an enemy's stand";
		lose[17] = "Get outta here lung boy";
		lose[18] = "I have the high ground";
		lose[19] = "It was me all along";
		lose[20] = "Why are you buying clothes at the soup store!?";
		lose[21] = "Natural 1";
		lose[22] = "Click the circles";
		lose[23] = "Do better next time";
		lose[24] = "This is a chicken wing";
		lose[25] = "yikes";
		lose[26] = "Why?";
		lose[27] = "nice";
		lose[28] = "Wow, such game";
		lose[29] = "Number of people with a higher score: " + (int)(10000.0 * Math.random());
		lose[30] = "Congratulations! You are the not the 100th player!";
		lose[31] = "Congratulations    Congratulations     Congratulations     Congratulations     Congratulations";
		lose[32] = "segmentation fault";
		lose[33] = "y tho";
		lose[34] = "All your base are belong to us";
		lose[35] = "You must construct additional pylons";
		lose[36] = "A barbarian has captured your worker";
		lose[37] = "Whether we wanted it or not, we've stepped into a war with the Cabal on Mars. \nSo let's get to taking out their command, one by one.";
		lose[38] = "This cosmic dance of bursting decadence and withheld permissions twists all \nout arms collectively; but if sweetness can win, and it can, \nthen I'll still be here tomorrow, to high-five you yesterday, my friend. \nPeace";
		lose[39] = "Uh Oh Stinky";
		lose[40] = "This cannot continue";
		lose[41] = "Sucking at something is the first step towards being sorta good at something";
		lose[42] = "Null pointer exception";
		lose[43] = "https://youtu.be/dQw4w9WgXcQ for the perfect score speedrun";
		lose[44] = "this is a sentence";
		
		return lose[index];
	}

    @Override
	public void render(float delta) {
		ScreenUtils.clear(0.5f, 0.5f, 0.5f, 1);

		camera.update();
		game.batch.setProjectionMatrix(camera.combined);
        
		game.batch.begin();
		backgroundSprite.draw(game.batch);

		
		font.setColor(255,255,255,1);
		font.draw(game.batch,endSplash, 120, 400);
		//game.font.draw(game.batch, "Main menu screen, click to start", 100, 150);
		
		game.batch.end();

		if (Gdx.input.isKeyJustPressed(Keys.E)) {
			game.setScreen(new Level1(game,level_data,0,1));
			dispose();
		}
		
		if (Gdx.input.isKeyPressed(Keys.ESCAPE)) {
            Gdx.app.exit();
        }

		if (Gdx.input.isKeyJustPressed(Keys.SPACE)) {
			game.setScreen(new CreditsMenu(game,level_data,this));
			dispose();
		}

		if (Gdx.input.isKeyJustPressed(Keys.MINUS)) {
			endSplash = getDeathSplash();
		}

		/*
		if (Gdx.input.isKeyJustPressed(Keys.Y)) {
			game.setScreen(new EndMenu(game,level_data));
			dispose();
		}*/

	}

    @Override
	public void resize(int width, int height) {

	}

    @Override
	public void show() {

	}

    @Override
	public void hide() {

	}

    @Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

    @Override
	public void dispose() {
		
	}

}