package com.mygdx.game.infoClasses;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class BlockInfo {
    
    public int numBlocks;
    public Vector2[] blockPoints;
    public Texture[] textures;

    public BlockInfo(int n, Vector2[] points, Texture[] t) {
        numBlocks = n;
        blockPoints = points;
        textures = t;
    }

}
