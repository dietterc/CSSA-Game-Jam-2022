package com.mygdx.game;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Block {
    private World world;
    private Body physicsBody;
    private Camera camera;
    private Sprite bodySprite;
    private Sprite movingSprite;

    private boolean moving;
    private boolean mouseReleased;

    public Block(World w,int startX, int startY, Camera c) {

        camera = c;
        world = w;

        moving = false;
        mouseReleased = true;

        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.position.set(startX,startY);

        physicsBody = world.createBody(groundBodyDef);
        PolygonShape groundBox = new PolygonShape();
        groundBox.setAsBox(1,.5f);
        physicsBody.createFixture(groundBox, 0.0f);
        groundBox.dispose();

        bodySprite = new Sprite(new Texture(Gdx.files.internal("placeholder_floor.png")));
        bodySprite.setBounds(0, 0, 2f, 1f);
        bodySprite.setOriginCenter();
        
        movingSprite = new Sprite(new Texture(Gdx.files.internal("placeholder_floor.png")));
        movingSprite.setBounds(0, 0, 2f, 1f);
        movingSprite.setOriginCenter();
        //movingSprite.setColor(.5f, .5f, .5f, 1);
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
                groundBox.setAsBox(1,.5f);
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

    

    public void dispose() {
        
    }

}
