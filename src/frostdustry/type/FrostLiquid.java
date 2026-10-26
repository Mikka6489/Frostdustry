package frostdustry.type;

import arc.graphics.*;
import mindustry.type.*;

import frostdustry.world.meta.*;

public class FrostLiquid extends Liquid{
    public int minHeatingRequired;
    public int maxHeatingRequired;
    
    public FrostLiquid(String name){
        super(name, new Color(Color.black));
    }

    @Override
    public void setStats(){
        super.setStats();
        stats.add(FrostStat.minHeat, minHeatingRequired);
        stats.add(FrostStat.maxHeat, maxHeatingRequired);
    }
}
