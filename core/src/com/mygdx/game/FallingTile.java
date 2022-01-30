package com.mygdx.game;

import java.util.ArrayList;

import com.badlogic.gdx.Input.Keys;
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
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class FallingTile extends Tile {

    private Float startinyHeight;
    private Boolean fallingNow;
    private Float dropSpeed = -1f;
    public Level1 level;
    private Boolean transformSelf = false;

    public FallingTile(World w,float startX, float startY, Camera c, Texture texture) {
        super(w, startX, startY, c, texture, "falling");
        startinyHeight = startY;
        fallingNow = false;
    }


    public void step() {
        super.step();
        if (Gdx.input.isKeyJustPressed(Keys.P)) {
            playerLandInit();
        }
        
        if (Gdx.input.isKeyJustPressed(Keys.R)) {
            resetPos();
        }

        if (transformSelf) {
            System.out.println("FallingTile step");
            transformSelf = false;
            physicsBody.setType(BodyType.KinematicBody);
            physicsBody.setLinearVelocity(0f,dropSpeed);
        }
    }


    public void playerLandInit() {
        if (!fallingNow) {
            for (Tile t: myBlock.tiles) {
                FallingTile ft = (FallingTile)t;
                ft.playerLandProc();
            }
        }
    }

    public void playerLandProc() {
        startinyHeight = physicsBody.getPosition().y;
        //physicsBody.setType(BodyType.KinematicBody);
        transformSelf = true;
        //level.fallingTiles.add(this);
        fallingNow = true;
    }

    public void resetPos() {
        physicsBody = createPhysicsBody(physicsBody.getPosition().x,startinyHeight,this);
        fallingNow = false;
        //level.fallingTiles.remove(this);
        //level.resetTiles.add(this);
    }


    
}
