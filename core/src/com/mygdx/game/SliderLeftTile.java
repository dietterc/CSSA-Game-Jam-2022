package com.mygdx.game;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;

public class SliderLeftTile extends Tile {

    public SliderLeftTile(World w,float startX, float startY, Camera c, Texture texture, Level1 level) {
        super(w, startX, startY, c, texture, "sliderleft", level);
    }
    
}
