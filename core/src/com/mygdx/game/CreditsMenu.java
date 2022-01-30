package com.mygdx.game;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.infoClasses.BlockInfo;
import com.mygdx.game.infoClasses.LevelInfo;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.graphics.glutils.FileTextureData;

public class CreditsMenu implements Screen {

	World world;
	final MyGdxGame game;
	LevelInfo[] level_data;
	public static Texture backgroundTexture;
    public static Sprite backgroundSprite;
	private Screen returnTo;


	OrthographicCamera camera;

	public CreditsMenu(final MyGdxGame game, LevelInfo[] level_data, Screen returnTo) {
		this.game = game;
		this.level_data = level_data;
		world = new World(new Vector2(0,0), true);
		this.returnTo = returnTo;

		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1920, 1080);

		backgroundTexture = new Texture("CreditsV2.png");
        backgroundSprite = new Sprite(backgroundTexture);
		//backgroundSprite.setBounds(0, 0, 1f, 1f);
        //backgroundSprite.setOriginCenter();
		//unstoreBlock(801f,555f);
	

	}

	
    @Override
	public void render(float delta) {
		ScreenUtils.clear(0.5f, 0.5f, 0.5f, 1);

		camera.update();
		game.batch.setProjectionMatrix(camera.combined);
        
		game.batch.begin();
		backgroundSprite.draw(game.batch);

		//System.out.println("Mouse: "+Gdx.input.getX()+", "+Gdx.input.getY());
		//game.font.draw(game.batch, "Main menu screen, click to start", 100, 150);
		
		game.batch.end();

		if (Gdx.input.isKeyJustPressed(Keys.P)) {
			game.setScreen(returnTo);
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