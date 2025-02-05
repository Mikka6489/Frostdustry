package frostdustry.world;

import arc.*;
import arc.util.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.ui.*;
import mindustry.world.*;
import mindustry.world.meta.*;
import mindustry.world.blocks.*;

import frostdustry.content.*;
import frostdustry.world.meta.*;

public class FrostBlock extends Block{
    @Deprecated
    
    public final Attribute coldattr = FrostAttribute.cold;
    public Attributes attrs = new Attributes();

    private transient float timeScale = 1f, timeScaleDuration;

    public boolean canBeHeated = true;

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
        stats.add(FrostStat.cold, (coldattr.env() * 10) + "°C");
    }

    @Override
    public void setBars(){
        super.setBars();
        if(!displayEfficiency) return;

        addBar("Heat", (FrostBuilding entity) ->
            new Bar(
            () -> Core.bundle.format("bar.cold", (int)(entity.efficiencyMultiplier() * 10 * displayEfficiencyScale)),
            () -> Pal.lightOrange,
            entity::efficiencyMultiplier));
    }

    public void applyHeat(float intensity, float duration) {
        // too lazy to fix Heater.java
        if (intensity >= this.timeScale - 0.001F) {
            this.timeScaleDuration = Math.max(this.timeScaleDuration, duration);
        }

        this.timeScale = Math.max(this.timeScale, intensity);
        Log.info("current value of cold: " + FrostAttribute.cold.env());
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
            return baseEfficiency + Math.min(maxBoost, boostScale * attrsum) - FrostAttribute.cold.env();
        }

    }
}
