package frostdustry.world;

import arc.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.ui.*;
import mindustry.world.*;
import mindustry.world.meta.*;

import frostdustry.content.*;
import frostdustry.world.meta.*;

public class FrostBlock extends Block{
    @Deprecated
    
    public final Attribute attribute = FrostAttribute.cold;
    public float baseEfficiency = 1f;
    public float boostScale = 1f;
    public float maxBoost = 1f;
    public float minEfficiency = -1f;
    public float displayEfficiencyScale = 1f;
    public boolean displayEfficiency = true;
    public boolean scaleLiquidConsumption = false;

    public FrostBlock(String name){
        super(name);
    }

    @Override
    public void setStats(){
        super.setStats();

        stats.add(FrostStat.cold, "-" + (attribute.env() * 10) + "°C");
    }

    @Override
    public void setBars(){
        super.setBars();
        if(!displayEfficiency) return;

        addBar("efficiency", (FrostBuilding entity) ->
            new Bar(
            () -> Core.bundle.format("bar.efficiency", (int)(entity.efficiencyMultiplier() * 100 * displayEfficiencyScale)),
            () -> Pal.lightOrange,
            entity::efficiencyMultiplier));
    }

    public class FrostBuilding extends Building {
        public float attrsum;

        @Override
        public float getProgressIncrease(float base){
            return super.getProgressIncrease(base) * efficiencyMultiplier();
        }

        @Override
        public float efficiencyScale(){
            return scaleLiquidConsumption ? efficiencyMultiplier() : super.efficiencyScale();
        }

        public float efficiencyMultiplier(){
            return baseEfficiency + Math.min(maxBoost, boostScale * attrsum) - attribute.env();
        }

    }
}

