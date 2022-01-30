package com.mygdx.game;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.infoClasses.BlockInfo;
import com.mygdx.game.infoClasses.LevelInfo;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.graphics.glutils.FileTextureData;



public class Level1 implements Screen {

	final MyGdxGame game;
    final int WIDTH = 20;
    final float HEIGHT = 11.25f;

	static OrthographicCamera camera;
    World world;
    Box2DDebugRenderer debugRenderer;
    Body body;
    Player player;
    LevelInfo[] level_data;
    static int levelNum;

    TiledMap tiledMap;
    TiledMapRenderer tiledMapRenderer;

    ArrayList<Tile> tiles = new ArrayList<Tile>();
    ArrayList<Block> blocks = new ArrayList<Block>();
    ArrayList<Sprite> blockSprites = new ArrayList<Sprite>();

    ArrayList<FallingTile> fallingTiles = new ArrayList<FallingTile>();

    boolean toReset = false;


	public Level1(final MyGdxGame game, LevelInfo[] level_d, int num) {
		this.game = game;
        level_data = level_d;
        levelNum = num;
        System.out.println("THIS ONE " + levelNum);

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

        LevelInfo mapData = level_data[levelNum];

        for(BlockInfo block : mapData.blocks) {
            Tile[] local_tiles = new Tile[block.numBlocks];
            int tilesIndex = 0;

            for(int i=0;i<block.numBlocks;i++) {

                float x1 = block.blockPoints[i].x;
                float y1 = block.blockPoints[i].y;
                Vector3 input = new Vector3(x1, y1, 0);
                camera.unproject(input);
                Tile tile;
                
                switch (((FileTextureData)block.textures[i].getTextureData()).getFileHandle().path()) {
                    //case "levels/start_block.png" :
                        
                    //break;
                    //case "levels/end_block.png" :

                    //break;
                    case "levels/spring_block.png" :
                        tile = new BouncyTile(world,input.x,input.y,camera,block.textures[i]);
                    break;
                    case "levels/no_jump_block.png" :
                        tile = new StickyTile(world,input.x,input.y,camera,block.textures[i]);
                    break;
                    case "levels/falling_block.png" :
                        tile = new FallingTile(world,input.x,input.y,camera,block.textures[i]);
                        FallingTile trueTile = (FallingTile)tile;
                        trueTile.level = this;
                        fallingTiles.add((FallingTile)tile);
                    break;
                    default:
                        tile = new Tile(world,input.x,input.y,camera,block.textures[i]);
                    break;
                }
                tile.texture = block.textures[i];
                tiles.add(tile);
                local_tiles[tilesIndex++] = tile; 
            }
            Block localBlock = new Block(local_tiles);
            blocks.add(localBlock);
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

        /*
        for(FallingTile tile : fallingTiles) {
            tile.step();
        }
        */

        player.step();
        for(int i=0;i<tiles.size();i++){
            tiles.get(i).step();
        }

        debugRenderer.render(world, camera.combined);
		world.step(1/60f, 6, 2);


        if(Gdx.input.isKeyPressed(Keys.P)) {

            if (toReset) {
                toReset = false;
                LevelInfo[] newLevelData = updateLevelData();
                level_data = newLevelData;
                //blocks.clear();
                tiles.clear();

                if(levelNum == 0)
                    game.setScreen(new MainMenu(game,newLevelData));
                else
                    game.setScreen(new Level1(game,newLevelData,levelNum--));
            } else {
                for (FallingTile t: fallingTiles) {
                    t.resetPos();
                }
                toReset = true;
            }

            
        }

        if(Gdx.input.isKeyJustPressed(Keys.L)) {

            LevelInfo[] newLevelData = updateLevelData();
            level_data = newLevelData;
            //blocks.clear();
            //tiles.clear();
            levelNum += 1;

            game.setScreen(new Level1(game,newLevelData,levelNum));
        
        }
        

	}

    private LevelInfo[] updateLevelData() {
        LevelInfo[] newInfo = new LevelInfo[level_data.length];

        for(int i=0; i<level_data.length; i++) {

            if(level_data[i] != null && level_data[i].levelNum != levelNum) {
                newInfo[i] = level_data[i];
                
            }
            else if(level_data[i] != null) {
                
                BlockInfo[] newBlocks = new BlockInfo[blocks.size()];

                for(int j=0; j<blocks.size(); j++) {
                    Block curr = blocks.get(j);

                    //System.out.println(curr.tiles[0].texture.toString());

                    Vector2[] points = new Vector2[curr.tiles.length];
                    Texture[] textures = new Texture[curr.tiles.length];
                    for(int k=0; k<curr.tiles.length; k++) {
                        float x1 = curr.tiles[k].bodySprite.getX();
                        float y1 = -curr.tiles[k].bodySprite.getY();
                        Vector3 input = new Vector3(x1, y1, 0);
                        camera.project(input);

                        points[k] = new Vector2(input.x+33.75f,input.y-33.75f);
                        textures[k] = curr.tiles[k].texture;
                        //System.out.println(curr.tiles[k].texture.toString());
                    }

                    newBlocks[j] = new BlockInfo(curr.tiles.length, points, textures);
                }

                newInfo[i] = new LevelInfo(levelNum, newBlocks);
            }

        }

        return newInfo;

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