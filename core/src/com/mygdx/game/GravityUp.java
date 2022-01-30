package com.mygdx.game;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;



public class GravityUp extends Tile {

    private Player player;

    public GravityUp(World w,float startX, float startY, Camera c, Texture texture, Level1 level, Player player) {
        super(w, startX, startY, c, texture, "gravityup", level);
        super.becomeSensor();
        this.player = player;
    }

    public void step() {
        super.step();
        if (player!= null) {
            if(bodySprite.getBoundingRectangle().contains(player.pos.x,player.pos.y)) {
                player.gravityDirection = "up";
                player.resumeGravityDown = -1;
            }
        }
    }
    
}
