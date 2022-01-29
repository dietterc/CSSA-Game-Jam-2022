package com.mygdx.game.infoClasses;

import com.mygdx.game.Tile;

public class LevelInfo {
    public int levelNum;
    public BlockInfo[] blocks;

    public LevelInfo(int n, BlockInfo[] otherBlocks) {
        levelNum = n;
        blocks = otherBlocks;
    }

}
