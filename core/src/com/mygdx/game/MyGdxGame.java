package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.infoClasses.BlockInfo;
import com.mygdx.game.infoClasses.LevelInfo;

public class MyGdxGame extends Game {
	public SpriteBatch batch;
	public BitmapFont font;
	public static LevelInfo[] level_data;
	
	@Override
	public void create () {
		level_data = new LevelInfo[13];

		for(int i=0;i<1;i++) {

			//load in level Data
			//TiledMap tiledMap = new TmxMapLoader().load("levels/level" + i + ".tmx");
			TiledMap tiledMap = new TmxMapLoader().load("levels/testlevel.tmx");

			MapLayers layers = tiledMap.getLayers();
			BlockInfo[] level1Blocks = new BlockInfo[layers.getCount()];
			int layerIndex = 0;

			for(MapLayer layer : layers) {
				System.out.println("Importing layer " + layer.getName() + " for level " + i);
				int blockIndex = 0;

				MapObjects objects = layer.getObjects();
				Vector2[] points = new Vector2[objects.getCount()];
				Texture[] textures = new Texture[objects.getCount()];

				for(MapObject object : objects) {

					TextureMapObject obj = (TextureMapObject) object;
					
					points[blockIndex] = new Vector2(obj.getX()+32,1080-obj.getY()-32);
					//System.out.println("HERE: " + (1080-obj.getY()-32));
					textures[blockIndex] = obj.getTextureRegion().getTexture();
					blockIndex += 1;
					
				}
				level1Blocks[layerIndex++] = new BlockInfo(objects.getCount(), points, textures);
			}
			level_data[i] = new LevelInfo(i, level1Blocks);

		}

		


		batch = new SpriteBatch();
		font = new BitmapFont(); // use libGDX's default Arial font for now
		this.setScreen(new MainMenu(this,level_data));

	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		font.dispose();
	}
}
