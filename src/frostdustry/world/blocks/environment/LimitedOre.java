package frostdustry.world.blocks.environment;

import arc.math.*;
import mindustry.world.Tile;
import mindustry.world.blocks.environment.*;
import mindustry.content.*;

public class LimitedOre extends OreBlock{
    private static final int maxOre = 1000;

    public LimitedOre(String name){
        super(name);
	saveData = true;
    }

    public void seedRandom(Tile tile){
        if(tile == null) return;
        int count = Mathf.random(300, maxOre);
        tile.extraData = count;
    }

    public int getOreCount(Tile tile){
        return tile.extraData;
    }

    public void setOreCount(Tile tile, int count){
        if(count <= 0){
            tile.extraData = 0;
            tile.setOverlay(Blocks.air);
        }else{
            tile.extraData = count;
        }
    }
}
