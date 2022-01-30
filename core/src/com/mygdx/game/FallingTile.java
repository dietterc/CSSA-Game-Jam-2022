package com.mygdx.game;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class FallingTile extends Tile {

    public Float startingHeight;
    private Boolean fallingNow;
    private Float dropSpeed = -1f;
    private Boolean transformSelf = false;


    public FallingTile(World w,float startX, float startY, Camera c, Texture texture, Level1 level) {
        super(w, startX, startY, c, texture, "falling", level);
        startingHeight = startY;
        fallingNow = false;
    }


    public void step() {
        super.step();
        //if (Gdx.input.isKeyJustPressed(Keys.P)) {
        //    playerLandInit();
        //}
        
        //if (Gdx.input.isKeyJustPressed(Keys.R)) {
        //    resetPos();
        //}

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
        startingHeight = physicsBody.getPosition().y;
        //physicsBody.setType(BodyType.KinematicBody);
        transformSelf = true;
        //level.fallingTiles.add(this);
        fallingNow = true;
    }

    public void resetPos() {
        physicsBody = createPhysicsBody(physicsBody.getPosition().x,startingHeight,this);
        fallingNow = false;
        level.fallingTileCount += 1;
        System.out.println("Falling Tiles "+level.fallingTileCount+" / "+level.fallingTiles.size());
        //level.fallingTiles.remove(this);
        //level.resetTiles.add(this);
    }


    
}
