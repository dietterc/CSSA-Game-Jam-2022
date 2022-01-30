package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.infoClasses.LevelInfo;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class MainMenu implements Screen {

	final MyGdxGame game;
	LevelInfo[] level_data;
	public static Texture backgroundTexture;
    public static Sprite backgroundSprite;

	OrthographicCamera camera;

	public MainMenu(final MyGdxGame game, LevelInfo[] level_data) {
		this.game = game;
		this.level_data = level_data;

		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1920, 1080);

		backgroundTexture = new Texture("AwesomeSpatialEccentricCrustaceanPuzzleTitleScreenKEKW.png");
        backgroundSprite = new Sprite(backgroundTexture);
		//backgroundSprite.setBounds(0, 0, 1f, 1f);
        //backgroundSprite.setOriginCenter();
	}

    @Override
	public void render(float delta) {
		ScreenUtils.clear(0.5f, 0.5f, 0.5f, 1);

		camera.update();
		game.batch.setProjectionMatrix(camera.combined);
        
		game.batch.begin();
		backgroundSprite.draw(game.batch);

		//game.font.draw(game.batch, "Main menu screen, click to start", 100, 150);
		
		game.batch.end();

		if (Gdx.input.isKeyJustPressed(Keys.E)) {
			game.setScreen(new Level1(game,level_data,0,1));
			dispose();
		}
		
		if (Gdx.input.isKeyPressed(Keys.ESCAPE)) {
            Gdx.app.exit();
        }

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