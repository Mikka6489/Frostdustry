package frostdustry.world.blocks.environment;

import arc.struct.*;
import arc.math.*;
import mindustry.world.Tile;
import mindustry.world.blocks.environment.*;
import mindustry.content.*;

public class FrostOre extends OreBlock{
    private static final int maxOre = 20;
    private final IntMap<Integer> oreCounts = new IntMap<>();

    public FrostOre(String name){
        super(name);
    }

    public void seedRandom(Tile tile){
        if(tile == null) return;
        int count = Mathf.random(3, maxOre);
        oreCounts.put(tile.pos(), count);
    }

    public int getOreCount(Tile tile){
        return oreCounts.get(tile.pos(), 0);
    }

    public void setOreCount(Tile tile, int count){
        if(count <= 0){
            oreCounts.remove(tile.pos());
            tile.setOverlay(Blocks.air);
        }else{
            oreCounts.put(tile.pos(), count);
        }
    }
}