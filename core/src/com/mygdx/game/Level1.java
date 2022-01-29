package com.mygdx.game;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.infoClasses.BlockInfo;
import com.mygdx.game.infoClasses.LevelInfo;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.graphics.glutils.FileTextureData;



public class Level1 implements Screen {

	final MyGdxGame game;
    final int WIDTH = 20;
    final float HEIGHT = 11.25f;

	OrthographicCamera camera;
    static World world;
    Box2DDebugRenderer debugRenderer;
    Body body;
    Player player;
    LevelInfo[] level_data;

    TiledMap tiledMap;
    TiledMapRenderer tiledMapRenderer;

    static ArrayList<Tile> tiles = new ArrayList<Tile>();
    static ArrayList<Sprite> blockSprites = new ArrayList<Sprite>();

	public Level1(final MyGdxGame game, LevelInfo[] level_data) {
		this.game = game;
        this.level_data = level_data;

		camera = new OrthographicCamera(WIDTH,HEIGHT);
        //world = new World(new Vector2(0,-10), true);
        world = new World(new Vector2(0,0), true);
        world.setContactListener(PlayerCollisions.createCollisionListener());
        debugRenderer = new Box2DDebugRenderer();

        player = new Player(world,0,0,camera);

        /*
        //global floor      -- for now
        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.position.set(0,-4);
        Body groundBody = world.createBody(groundBodyDef);
        PolygonShape groundBox = new PolygonShape();
        groundBox.setAsBox(32, 1);
        groundBody.createFixture(groundBox, 0.0f);
        groundBox.dispose();
        */
        
        LevelInfo mapData = level_data[0];

        for(BlockInfo block : mapData.blocks) {
            Tile[] local_tiles = new Tile[block.numBlocks];
            int tilesIndex = 0;

            for(int i=0;i<block.numBlocks;i++) {

                float x1 = block.blockPoints[i].x;
                float y1 = block.blockPoints[i].y;
                Vector3 input = new Vector3(x1, y1, 0);
                camera.unproject(input);
                Tile tile = new Tile(world,input.x,input.y,camera,block.textures[i]);
                switch (((FileTextureData)block.textures[i].getTextureData()).getFileHandle().path()) {
                    case "levels/start_block.png" :
                        
                    break;
                    case "levels/end_block.png" :

                    break;
                    case "levels/spring_block.png" :
                        tile = new BouncyTile(world,input.x,input.y,camera,block.textures[i]);
                    break;
                    default:
                        tile = new Tile(world,input.x,input.y,camera,block.textures[i]);
                    break;
                }
                tiles.add(tile);
                local_tiles[tilesIndex++] = tile; 
            }
            Block localBlock = new Block(local_tiles);
            //MyGdxGame.active_blocks[activeBlocksIndex++] = localBlock;
            for(int i=0;i<localBlock.tiles.length;i++) {
                localBlock.tiles[i].setBlock(localBlock);
            }
        }
	}



    @Override
	public void render(float delta) {
		ScreenUtils.clear(0f, 0f, 0f, 1);

		camera.update();
		game.batch.setProjectionMatrix(camera.combined);

		game.batch.begin();
        game.batch.setProjectionMatrix(camera.combined);

        player.draw(game.batch);
        for(int i=0;i<tiles.size();i++){
            tiles.get(i).draw(game.batch);   
        }
        
		game.batch.end();

        player.step();
        for(int i=0;i<tiles.size();i++){
            tiles.get(i).step();
        }

        debugRenderer.render(world, camera.combined);
		world.step(1/60f, 6, 2);


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
        //player.dispose();
	}

}