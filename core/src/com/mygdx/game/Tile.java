package com.mygdx.game;

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

    private World world;
    private Body physicsBody;
    private Camera camera;
    private Sprite bodySprite;
    private Sprite movingSprite;

    private boolean moving;
    private boolean mouseReleased;

    public String label = "block";

    public Tile(World w,float startX, float startY, Camera c, Texture texture) {

        camera = c;
        world = w;

        moving = false;
        mouseReleased = true;

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
        world = w;

        moving = false;
        mouseReleased = true;

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
                    bodySprite.setAlpha(.5f);
                    moving = true;
                    mouseReleased = false;
                }
            }
        }
        else {
            if (Gdx.input.isTouched() && mouseReleased) {
                moving = false;
                mouseReleased = false;
                bodySprite.setAlpha(1);

                int x1 = Gdx.input.getX();
                int y1 = Gdx.input.getY();
                Vector3 input = new Vector3(x1, y1, 0);
                camera.unproject(input);

                BodyDef groundBodyDef = new BodyDef();
                groundBodyDef.position.set(input.x,input.y);

                world.destroyBody(physicsBody);

                physicsBody = world.createBody(groundBodyDef);
                PolygonShape groundBox = new PolygonShape();
                groundBox.setAsBox(PHYISCS_DIM,PHYISCS_DIM);
                physicsBody.createFixture(groundBox, 0.0f);
                groundBox.dispose();
            }
        }

    }
    
    //called every iteration of render
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

    }

    public String toString() {
        return label;
    }

    public void dispose() {
        
    }

}
