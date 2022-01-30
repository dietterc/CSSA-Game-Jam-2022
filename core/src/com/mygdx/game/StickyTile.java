package com.mygdx.game;


import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;

import com.badlogic.gdx.physics.box2d.World;

public class StickyTile extends Tile {

    public String gravityDirection = "down";

    public StickyTile(World w,float startX, float startY, Camera c, Texture texture, Level1 level) {
        super(w, startX, startY, c, texture, "sticky", level);
    }

    public void step() {
        super.step();
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
    }
    
}
