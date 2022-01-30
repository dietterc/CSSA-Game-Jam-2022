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

    private boolean editMode = false;


    private Float baseMoveSpeed = 0.2f; //unused

    private Float SPEED = 16f;
    private Float MAX_VELOCITY = 90f;
    private Float undrift = 0.1f;

    private Float bouncePower = 2.75f;
    private Float baseJumpStrength = 3.1f;
    private Float baseJumpDecay = 0.265f;

    private Float gravityStrength = -40f;
    public String gravityDirection = "down";
    public int resumeGravityDown = -1;

    private Float fallingThreshold = 1f;
    public Boolean landed = false;
    public Boolean landedOnFalling = false;
    private Float jumping = 0f;

    private Vector2 speed = new Vector2(0f,0f);
    public Vector2 pos = new Vector2(0f,0f);

    public String label = "Travis";

    public Boolean sticky = false;
    public Boolean sliderleft = false;
    public Boolean sliderright = false;
    public Boolean gravityUp = false;
    public Boolean bounced = false;

    public Level1 level;

    public static Block storedBlock;

    private Sprite crabPlain;
    private Sprite crabBag;

    public Player(World world,float startX, float startY, OrthographicCamera c) {
        
        camera = c;

        //Set up physics body as a circle
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(startX, startY); //starting position
        physicsBody = world.createBody(bodyDef);
        physicsBody.setFixedRotation(true);

        circle = new CircleShape();
        circle.setRadius(.24f);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 0.0f;
        fixtureDef.friction = 0.1f;
        fixtureDef.restitution = 0.0f;
        Fixture fixture = physicsBody.createFixture(fixtureDef);

        crabPlain = new Sprite(new Texture(Gdx.files.internal("space_crab.png")));
        crabPlain.setBounds(0, 0, 1f, 1f);
        crabPlain.setOriginCenter(); 
        //crabPlain.set(new Sprite(new Texture(Gdx.files.internal("space_crab.png"))));
        crabBag = new Sprite(new Texture(Gdx.files.internal("space_crab_withbag.png")));
        crabBag.setBounds(0, 0, 1f, 1f);
        crabBag.setOriginCenter(); 
        if (storedBlock != null)
            setSprite("bag");
        else
            setSprite("none");

        //bodySprite = crabPlain;
        //bodySprite.setBounds(0, 0, 1f, 1f);
        //bodySprite.setOriginCenter();        

        physicsBody.setUserData(this);

    }

    public void setSprite(String sprite) {
        if (sprite == "bag") {
            bodySprite = crabBag;
        } else if (sprite == "none") {
            bodySprite = crabPlain;
        }
    }

    public void setEditMode(boolean val) {
        editMode = val;
    }

    private void applyForce(float x, float y) {
        physicsBody.applyLinearImpulse(x,y,pos.x,pos.y,true);
    }

    public void bounce(String direction) {
        System.out.println("Travis: Bounce?");
        if (direction == "up") {
            bounced = true;
            System.out.println("Travis: Up!");
            jumping = bouncePower;
            physicsBody.setLinearVelocity(new Vector2(physicsBody.getLinearVelocity().x,-physicsBody.getLinearVelocity().y/2));
            System.out.println("jumping " +jumping);
        } else if (direction == "down") {
            bounced = true;
            System.out.println("Travis: Down!");
            jumping = bouncePower;
            physicsBody.setLinearVelocity(new Vector2(physicsBody.getLinearVelocity().x,-physicsBody.getLinearVelocity().y/2));
            //applyForce(0f,-20f);
        }
    }

    private void manageJump() {
        //continue jump height
        if (jumping > 0f) {
            if (Gdx.input.isKeyPressed(Keys.SPACE) || bounced) {
                jumping -= baseJumpDecay;
                landed = false;
                System.out.println("Landed: "+landed);
            } 
            //stop jumping and start falling
            if (!Gdx.input.isKeyPressed(Keys.SPACE) && !bounced) {
                jumping = 0f;
            }
        } else if (jumping != 0f) {
            jumping = 0f;
        }
        
        switch (gravityDirection) {
            case "down" :
                //physicsBody.applyLinearImpulse(0f,jumping,pos.x,pos.y,true);
                applyForce(0f,jumping);
            break;
            case "up" :
                //physicsBody.applyLinearImpulse(0f,-jumping,pos.x,pos.y,true);
                applyForce(0f,-jumping);
            break;
            /*case "left" :
                //physicsBody.applyLinearImpulse(jumping,0f,pos.x,pos.y,true);
                applyForce(jumping,0f);
            break;
            case "right" :
                //physicsBody.applyLinearImpulse(-jumping,0f,pos.x,pos.y,true);
                applyForce(-jumping,0f);
            break;*/
        }
        
    }   
    

    private void updateVars() {
        speed = physicsBody.getLinearVelocity();
        pos = physicsBody.getPosition();
        checkFalling();
        //updateLanded();
        if (gravityDirection != "up" && gravityDirection != "down" && gravityDirection != "left" && gravityDirection != "right") {
            gravityDirection = "down";
            //System.out.println("Invalid Gravity Direction! Setting to Down");
        }
        manageGravity();
        
    }

    private void manageGravity() {
        if (level.gravityTiles.size() > 0) {
            boolean toChange = false;
            for(GravityUp g: level.gravityTiles) {
                if(bodySprite.getBoundingRectangle().contains(g.getPosition())) {
                    toChange = true;
                }
                //if (toChange)
                //    break;
            }
            if (toChange) {
                gravityDirection = "up";

                System.out.println("Gravity Up");
                bodySprite.setFlip(false,true);
            } else {
                gravityDirection = "down";
                System.out.println("Gravity Down");
                bodySprite.setFlip(false,false);
            }
        }
        /*
        if (resumeGravityDown == 0){
            resumeGravityDown --;
            gravityDirection = "down";
        } else if (resumeGravityDown > 0) {
            resumeGravityDown --;
        }*/
    }

    private void checkFalling() {
        if (!landedOnFalling) {
            if (speed.y < -fallingThreshold || speed.y > fallingThreshold) {
                landed = false;
                //System.out.println("checkFalling Landed: "+landed);
            }
        }
    }

    private void updateLanded() {
        if (gravityDirection == "down" || gravityDirection == "up") {
            if (speed.y == 0f) {
                landed = true;
                System.out.println("Landed: "+landed);
                bounced = false;
                jumping = 0f;
            } else {
                landed = false;
                System.out.println("Landed: "+landed);
            }  
        /*} else {
            if (speed.x == 0f) {
                landed = true;
                jumping = 0f;
            } else {
                landed = false;
            } */ 
        }
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
        //checkControls();
        manageMovement();
        manageJump();
        updateVars();
        applyGravity();
        //escape to exit game
        if (Gdx.input.isKeyPressed(Keys.ESCAPE)) {
            Gdx.app.exit();
        }
        if (Gdx.input.isKeyPressed(Keys.P)) {
            level.game.setScreen(new MainMenu(level.game,level.level_data));
        }
        //DEBUGGING!! Remove these later ----------
        /*
        debugGravityChange();

        if (Gdx.input.isKeyJustPressed(Keys.T)) {
            System.out.println("bag");
            setSprite("bag");
        }*/
        //-----------------------------------------
    }
    
    //called every iteration of render
    public void draw(SpriteBatch batch) {
        Vector2 worldPos = physicsBody.getPosition();

        bodySprite.setPosition(worldPos.x - (bodySprite.getWidth() / 2), worldPos.y - (bodySprite.getHeight() / 2));
        bodySprite.draw(batch);

    }

    public String toString() {
        return label;
    }

    public void dispose() {
        circle.dispose();
    }

    private void manageMovement() {
        if (!editMode) {
            if (Gdx.input.isKeyJustPressed(Keys.SPACE) && (landed || landedOnFalling) && jumping == 0 && !sticky) {
                //System.out.println("Landed: "+landed);
                //System.out.println("Landed on Falling: "+landedOnFalling);
                jumping = baseJumpStrength;
                //System.out.println("Is Stick? "+sticky);
                //physicsBody.applyLinearImpulse(0f,jumping,pos.x,pos.y,true);
            }

            //add speed
            if (Gdx.input.isKeyPressed(Keys.A)) {
                physicsBody.applyForceToCenter(-SPEED, 0.0f, true);
            }
            if (Gdx.input.isKeyPressed(Keys.S)) {
                physicsBody.applyForceToCenter(SPEED, 0.0f, true);
            }
            
            //opposite speed
            if (!Gdx.input.isKeyPressed(Keys.A)) {
                if(physicsBody.getLinearVelocity().x < 0) 
                    physicsBody.applyForceToCenter(SPEED, 0.0f, true);
            }
            if (!Gdx.input.isKeyPressed(Keys.S)) {
                if(physicsBody.getLinearVelocity().x > 0) 
                    physicsBody.applyForceToCenter(-SPEED, 0.0f, true);
            }

            //max speed
            if(physicsBody.getLinearVelocity().x >= MAX_VELOCITY) {
                physicsBody.setLinearVelocity(MAX_VELOCITY,physicsBody.getLinearVelocity().y);
            } else if(physicsBody.getLinearVelocity().x <= -MAX_VELOCITY) {
                physicsBody.setLinearVelocity(-MAX_VELOCITY,physicsBody.getLinearVelocity().y);
            }
            
            //if close to 0, set to 0
            if(physicsBody.getLinearVelocity().x < undrift && physicsBody.getLinearVelocity().x > -undrift) {
                physicsBody.setLinearVelocity(0,physicsBody.getLinearVelocity().y);
            }

        }   
    }

    private void checkControls() {//jump
        if (!editMode) {
            if (Gdx.input.isKeyJustPressed(Keys.SPACE) && landed && jumping == 0) {
                jumping = baseJumpStrength;
                //physicsBody.applyLinearImpulse(0f,jumping,pos.x,pos.y,true);
            }
            switch (gravityDirection) {//left/right movement varies based on gravity
                case "down" :
                    //left
                    if (Gdx.input.isKeyPressed(Keys.S) && !Gdx.input.isKeyPressed(Keys.A)) {
                        applyForce(baseMoveSpeed,0f);
                    } 
                    //right
                    if (Gdx.input.isKeyPressed(Keys.A) && !Gdx.input.isKeyPressed(Keys.S)) {
                        applyForce(-baseMoveSpeed,0f);
                    }
                break;
                case "up" :
                    //left
                    if (Gdx.input.isKeyPressed(Keys.S) && !Gdx.input.isKeyPressed(Keys.A)) {
                        applyForce(baseMoveSpeed,0f);
                    } 
                    //right
                    if (Gdx.input.isKeyPressed(Keys.A) && !Gdx.input.isKeyPressed(Keys.S)) {
                        applyForce(-baseMoveSpeed,0f);
                    }
                break;
                case "left" :
                    //left
                    if (Gdx.input.isKeyPressed(Keys.S) && !Gdx.input.isKeyPressed(Keys.A)) {
                        applyForce(0f,-baseMoveSpeed);
                    } 
                    //right
                    if (Gdx.input.isKeyPressed(Keys.A) && !Gdx.input.isKeyPressed(Keys.S)) {
                        applyForce(0f,baseMoveSpeed);
                    }
                break;
                case "right" :
                    //left
                    if (Gdx.input.isKeyPressed(Keys.S) && !Gdx.input.isKeyPressed(Keys.A)) {
                        applyForce(0f,baseMoveSpeed);
                    } 
                    //right
                    if (Gdx.input.isKeyPressed(Keys.A) && !Gdx.input.isKeyPressed(Keys.S)) {
                        applyForce(0f,-baseMoveSpeed);
                    }
                break;
            }
        }
    }


}
