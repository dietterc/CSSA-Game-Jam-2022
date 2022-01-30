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

public class EndMenu implements Screen {

	World world;
	final MyGdxGame game;
	LevelInfo[] level_data;
	public static Texture backgroundTexture;
    public static Sprite backgroundSprite;
	private Block finalBlock;

	ArrayList<Tile> tiles = new ArrayList<Tile>();

	OrthographicCamera camera;

	public EndMenu(final MyGdxGame game, LevelInfo[] level_data, Block finalBlock) {
		this.game = game;
		this.level_data = level_data;
		this.finalBlock = finalBlock;
		world = new World(new Vector2(0,0), true);

		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1920, 1080);

		backgroundTexture = new Texture("YouWinVersion1.png");
        backgroundSprite = new Sprite(backgroundTexture);
		//backgroundSprite.setBounds(0, 0, 1f, 1f);
        //backgroundSprite.setOriginCenter();
		//unstoreBlock(801f,555f);
	

	}

	public void unstoreBlock(Float x, Float y) {
        Vector3 input = new Vector3(x, y, 0);
        camera.unproject(input);
		//for (Tile t:finalBlock.tiles) {
		//	t.
		//}

        for(int i=0;i<Player.storedBlock.tiles.length;i++) {
            Tile curr = Player.storedBlock.tiles[i];
            Tile tile = null;
            if(curr instanceof BouncyTile) {
                tile = new BouncyTile(world,input.x + curr.diffX,input.y + curr.diffY,camera,curr.texture,null);
            }
            else if (curr instanceof StickyTile) {
                tile = new StickyTile(world,input.x + curr.diffX,input.y + curr.diffY,camera,curr.texture,null);
            }
            else if (curr instanceof FallingTile) {
                tile = new FallingTile(world,input.x + curr.diffX,input.y + curr.diffY,camera,curr.texture,null);
            }
            else if ((curr instanceof Tile)) {
                tile = new Tile(world,input.x + curr.diffX,input.y + curr.diffY,camera,curr.texture,"",null);
            }
            tiles.add(tile);
			System.out.println("texture: "+curr.texture.toString());
		}
		
	}

    @Override
	public void render(float delta) {
		ScreenUtils.clear(0.5f, 0.5f, 0.5f, 1);

		camera.update();
		game.batch.setProjectionMatrix(camera.combined);
        
		game.batch.begin();
		backgroundSprite.draw(game.batch);

		for(int i=0;i<tiles.size();i++){
            tiles.get(i).draw(game.batch);   
        }
		//System.out.println("Mouse: "+Gdx.input.getX()+", "+Gdx.input.getY());
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