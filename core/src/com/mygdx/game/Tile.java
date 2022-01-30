package com.mygdx.game;

import java.lang.System.Logger.Level;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Tile {
    
    private final float SPRITE_DIM = .7f;
    private final float PHYISCS_DIM = .34f;

    public Body physicsBody;
    private Camera camera;
    public Sprite bodySprite;
    private Sprite movingSprite;
    private Block myBlock;
    public World world;

    private boolean moving;
    private boolean mouseReleased;
    private boolean otherMoving;

    public float diffX;
    public float diffY;

    public Texture texture;

    public String label = "block";

    public Tile(World w,float startX, float startY, Camera c, Texture texture) {

        camera = c;
        myBlock = null;
        world = w;

        moving = false;
        mouseReleased = true;
        otherMoving = false;

        diffX = 0f;
        diffY = 0f;

        this.texture = texture;

        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.position.set(startX,startY);

        physicsBody = world.createBody(groundBodyDef);
        PolygonShape groundBox = new PolygonShape();
        groundBox.setAsBox(PHYISCS_DIM,PHYISCS_DIM);
        physicsBody.createFixture(groundBox, 0.0f);
        groundBox.dispose();

        bodySprite = new Sprite(texture);
        bodySprite.setBounds(0, 0, SPRITE_DIM, SPRITE_DIM);
        bodySprite.setOriginCenter();
        
        movingSprite = new Sprite(texture);
        movingSprite.setBounds(0, 0, SPRITE_DIM, SPRITE_DIM);
        movingSprite.setOriginCenter();
        //movingSprite.setColor(.5f, .5f, .5f, 1);
    }

    public Tile(World w,float startX, float startY, Camera c, Texture texture, String label) {

        camera = c;
        myBlock = null;
        world = w;

        moving = false;
        mouseReleased = true;
        otherMoving = false;

        diffX = 0f;
        diffY = 0f;

        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.position.set(startX,startY);

        physicsBody = world.createBody(groundBodyDef);
        PolygonShape groundBox = new PolygonShape();
        groundBox.setAsBox(PHYISCS_DIM,PHYISCS_DIM);
        physicsBody.createFixture(groundBox, 0.0f);
        groundBox.dispose();

        bodySprite = new Sprite(texture);
        bodySprite.setBounds(0, 0, SPRITE_DIM, SPRITE_DIM);
        bodySprite.setOriginCenter();
        
        movingSprite = new Sprite(texture);
        movingSprite.setBounds(0, 0, SPRITE_DIM, SPRITE_DIM);
        movingSprite.setOriginCenter();
        //movingSprite.setColor(.5f, .5f, .5f, 1);

        this.label = label;
        
        physicsBody.setUserData(this);
    }

    public Tile(Tile oldTile) {
        this(oldTile.world, oldTile.bodySprite.getX(), oldTile.bodySprite.getY(), oldTile.camera, oldTile.texture);

    }

    public void becomeSensor() {
        for (Fixture f: physicsBody.getFixtureList()) {
            f.setSensor(true);
        }
    }
    
    //called every iteration of render
    public void step() {

        if(!Gdx.input.isTouched()){
            mouseReleased = true;
        }

        if(!moving && mouseReleased) {
            if (Gdx.input.isTouched()) {
                int x1 = Gdx.input.getX();
                int y1 = Gdx.input.getY();
                Vector3 input = new Vector3(x1, y1, 0);
                camera.unproject(input);
                //Now you can use input.x and input.y, as opposed to x1 and y1, to determine if the moving
                //sprite has been clicked
                if(bodySprite.getBoundingRectangle().contains(input.x, input.y)) {
                    if(myBlock != null) {
                        for(int i=0;i<myBlock.tiles.length;i++){
                            myBlock.tiles[i].bodySprite.setAlpha(.5f);
                            myBlock.tiles[i].diffX = myBlock.tiles[i].bodySprite.getX() - bodySprite.getX();
                            myBlock.tiles[i].diffY = myBlock.tiles[i].bodySprite.getY() - bodySprite.getY();
                            //System.out.println("Y: " + myBlock.tiles[i].diffY);
                        }
                    }
                
                    moving = true;
                    mouseReleased = false;
                }
            }
        }
        else {
            if (Gdx.input.isTouched() && mouseReleased) {
                moving = false;
                mouseReleased = false;

                int x1 = Gdx.input.getX();
                int y1 = Gdx.input.getY();
                Vector3 input = new Vector3(x1, y1, 0);
                camera.unproject(input);

                for(int i=0;i<myBlock.tiles.length;i++) {
                    myBlock.tiles[i].bodySprite.setAlpha(1);

                    BodyDef groundBodyDef = new BodyDef();
                    groundBodyDef.position.set(input.x + myBlock.tiles[i].diffX,input.y + myBlock.tiles[i].diffY);
                    System.out.println("X: " + myBlock.tiles[i].diffX);

                    world.destroyBody(myBlock.tiles[i].physicsBody);

                    myBlock.tiles[i].physicsBody = world.createBody(groundBodyDef);
                    PolygonShape groundBox = new PolygonShape();
                    groundBox.setAsBox(PHYISCS_DIM,PHYISCS_DIM);
                    myBlock.tiles[i].physicsBody.createFixture(groundBox, 0.0f);
                    groundBox.dispose();

                } 

            }
        }

    }
    
    public void draw(SpriteBatch batch) {
        Vector2 worldPos = physicsBody.getPosition();

        bodySprite.setPosition(worldPos.x - (bodySprite.getWidth() / 2), worldPos.y - (bodySprite.getHeight() / 2));
        bodySprite.draw(batch);

        if(moving) {
            int x1 = Gdx.input.getX();
            int y1 = Gdx.input.getY();
            Vector3 input = new Vector3(x1, y1, 0);
            camera.unproject(input);

            movingSprite.setPosition(input.x - (movingSprite.getWidth() / 2), input.y - (movingSprite.getHeight() / 2));
            movingSprite.draw(batch);
        }

        Tile leader = null;
        if(myBlock != null) {
            for(int i=0;i<myBlock.tiles.length;i++){
                if(myBlock.tiles[i].moving) {
                    if(myBlock.tiles[i] != this) {
                        otherMoving = true;
                        leader = myBlock.tiles[i];
                    }
                }
            }
        }

        if(otherMoving && leader != null ) {
            movingSprite.setPosition(leader.movingSprite.getX() + diffX, leader.movingSprite.getY() + diffY);
            movingSprite.draw(batch);
        }


    }

    public void setBlock(Block b) {
        myBlock = b;
    }

    public String toString() {
        return label;
    }

    public void dispose() {
        
    }

}
