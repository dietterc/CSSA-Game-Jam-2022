package com.mygdx.game;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;

public class EndTile extends Tile {

    public EndTile(World w,float startX, float startY, Camera c, Texture texture) {
        super(w, startX, startY, c, texture, "endtile");
    }
    
}
