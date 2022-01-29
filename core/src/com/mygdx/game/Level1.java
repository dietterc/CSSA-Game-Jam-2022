package com.mygdx.game;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.math.Vector2;

public class Level1 implements Screen {

	final MyGdxGame game;
    final int WIDTH = 20;
    final float HEIGHT = 11.25f;

	OrthographicCamera camera;
    World world;
    Box2DDebugRenderer debugRenderer;
    Body body;
    Player player;

    static ArrayList<Block> blocks = new ArrayList<Block>();
    static ArrayList<Sprite> blockSprites = new ArrayList<Sprite>();

	public Level1(final MyGdxGame game) {
		this.game = game;

		camera = new OrthographicCamera(WIDTH,HEIGHT);
        //world = new World(new Vector2(0,-10), true);
        world = new World(new Vector2(0,0), true);
        debugRenderer = new Box2DDebugRenderer();

        player = new Player(world,0,0,camera);

        //global floor      -- for now
        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.position.set(0,-4);
        Body groundBody = world.createBody(groundBodyDef);
        PolygonShape groundBox = new PolygonShape();
        groundBox.setAsBox(32, 1);
        groundBody.createFixture(groundBox, 0.0f);
        groundBox.dispose();

        //blocks
        blocks.add(new Block(world,0,-2,camera));
        blocks.add(new Block(world,4,-2,camera));

	}

    @Override
	public void render(float delta) {
		ScreenUtils.clear(0f, 0f, 0f, 1);

		camera.update();
		game.batch.setProjectionMatrix(camera.combined);

		game.batch.begin();
        game.batch.setProjectionMatrix(camera.combined);

        player.draw(game.batch);
        for(int i=0;i<blocks.size();i++){
            blocks.get(i).draw(game.batch);   
        }
        
		game.batch.end();

        player.step();
        for(int i=0;i<blocks.size();i++){
            blocks.get(i).step();
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