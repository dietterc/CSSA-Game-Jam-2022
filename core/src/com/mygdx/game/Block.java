package com.mygdx.game;

import com.mygdx.game.infoClasses.BlockInfo;

public class Block {

    Tile[] tiles;
    BlockInfo originalBlock;
    Level1 level;
    
    public Block(Tile[] tiles, BlockInfo originalBlock, Level1 level) {
        this.tiles = tiles;
        this.originalBlock = originalBlock;
        this.level = level;
    }

}
 