package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class Player {

    private Body physicsBody;
    private CircleShape circle;

    private Sprite bodySprite;

    private OrthographicCamera camera;


    private Float baseMoveSpeed = 7f;
    private Float baseJumpStrength = 2.1f;
    private Float baseJumpDecay = 0.25f;

    private Float gravityStrength = -10f;
    private String gravityDirection = "down";

    private Boolean landed = false;
    private Float jumping = 0f;

    private Vector2 speed = new Vector2(0f,0f);
    private Vector2 pos = new Vector2(0f,0f);

    public Player(World world,int startX, int startY, OrthographicCamera c) {
        
        camera = c;

        //Set up physics body as a circle
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(startX, startY); //starting position
        physicsBody = world.createBody(bodyDef);
        physicsBody.setFixedRotation(true);

        circle = new CircleShape();
        circle.setRadius(.35f);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 0.0f;
        fixtureDef.friction = 0.2f;
        fixtureDef.restitution = 0.0f;
        Fixture fixture = physicsBody.createFixture(fixtureDef);

        bodySprite = new Sprite(new Texture(Gdx.files.internal("space_crab.png")));
        bodySprite.setBounds(0, 0, 1.5f, 1.5f);
        bodySprite.setOriginCenter();        

    }



    private void applyForce(float x, float y) {
        physicsBody.applyForceToCenter(x,y,true);
    }

    private void checkControls() {
        //left
        if (Gdx.input.isKeyPressed(Keys.S) && !Gdx.input.isKeyPressed(Keys.A)) {
            applyForce(baseMoveSpeed,0f);
        } 
        //right
        if (Gdx.input.isKeyPressed(Keys.A) && !Gdx.input.isKeyPressed(Keys.S)) {
            applyForce(-baseMoveSpeed,0f);
        }
        //jump
        if (Gdx.input.isKeyPressed(Keys.SPACE) && landed && jumping == 0) {
            jumping = baseJumpStrength;
            //physicsBody.applyLinearImpulse(0f,jumping,pos.x,pos.y,true);
        }

    }

    private void manageJump() {
        //continue jump height
        if (jumping > 0f) {
            if (Gdx.input.isKeyPressed(Keys.SPACE)) {
                physicsBody.applyLinearImpulse(0f,jumping,pos.x,pos.y,true);
                jumping -= baseJumpDecay;
            } 
            //stop jumping and start falling
            if (!Gdx.input.isKeyPressed(Keys.SPACE)) {
                jumping = 0f;
            }
        } else if (jumping != 0f) {
            jumping = 0f;
        }
    }   

    private void updateVars() {
        speed = physicsBody.getLinearVelocity();
        if (speed.y == 0f) {
            landed = true;
            jumping = 0f;
        } else {
            landed = false;
        }
        pos = physicsBody.getPosition();
    }

    private void applyGravity() {
        switch (gravityDirection) {
            case "down" :
                physicsBody.applyForceToCenter(new Vector2(0,gravityStrength),true);
                break;
            case "up" :
                physicsBody.applyForceToCenter(new Vector2(0,-gravityStrength),true);
                break;
            case "left" :
                physicsBody.applyForceToCenter(new Vector2(gravityStrength,0),true);
                break;
            case "right" :
                physicsBody.applyForceToCenter(new Vector2(-gravityStrength,0),true);
                break;
        }
    }

    private void debugGravityChange() {
        if (Gdx.input.isKeyPressed(Keys.UP)) {
            gravityDirection = "up";
        }
        if (Gdx.input.isKeyPressed(Keys.DOWN)) {
            gravityDirection = "down";
        }
        if (Gdx.input.isKeyPressed(Keys.LEFT)) {
            gravityDirection = "left";
        }
        if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
            gravityDirection = "right";
        }
    }

    //called every iteration of render
    public void step() {
        checkControls();
        manageJump();
        updateVars();
        applyGravity();
        //escape to exit game
        if (Gdx.input.isKeyPressed(Keys.ESCAPE)) {
            Gdx.app.exit();
        }
        //DEBUGGING!! Remove these later ----------
        debugGravityChange();
        //-----------------------------------------
    }
    
    //called every iteration of render
    public void draw(SpriteBatch batch) {
        Vector2 worldPos = physicsBody.getPosition();

        bodySprite.setPosition(worldPos.x - (bodySprite.getWidth() / 2), worldPos.y - (bodySprite.getHeight() / 2));
        bodySprite.draw(batch);

    }

    public void dispose() {
        circle.dispose();
    }


}
