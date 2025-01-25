package frostdustry.world;

import arc.*;
import arc.util.Log;
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
    public boolean heated = false;

    private transient float timeScale = 1f, timeScaleDuration;

    public float baseEfficiency = 1f;
    public float boostScale = 1f;
    public float maxBoost = 1f;
    public float minEfficiency = -1f;
    public float displayEfficiencyScale = 1f;
    public boolean displayEfficiency = true;
    public boolean scaleLiquidConsumption = false;

    

    public FrostBlock(String name){
        super(name);
        attrs.set(coldattr, 0.75f);


    }

    @Override
    public void setStats(){
        super.setStats();
        stats.add(FrostStat.cold, "-" + (attrs.get(coldattr) * 10) + "°C");
//        stats.add(FrostStat.cold, "-" + (coldattr.env() * 10) + "°C");
    }

    @Override
    public void setBars(){
        super.setBars();
        if(!displayEfficiency) return;

        addBar("Heat", (FrostBuilding entity) ->
            new Bar(
            () -> Core.bundle.format("bar.cold", (int)(entity.efficiencyMultiplier() * 100 * displayEfficiencyScale)),
            () -> Pal.lightOrange,
            entity::efficiencyMultiplier));
    }

    public void applyHeat(float intensity, float duration) {
        //do not refresh time scale when getting a weaker intensity
        if(intensity >= this.timeScale - 0.001f){
            timeScaleDuration = Math.max(timeScaleDuration, duration);
        }
        timeScale = Math.max(timeScale, intensity);
    }

/*
    while (true) {
        // terrible but works
        if (!(heated)) {
            if (timeScale > 0) {
                this.attrs.set(this.coldattr, this.attrs.get(this.coldattr) + intensity);
            }
        else {
            if (timeScale = 0) {
                this.attrs.set(this.coldattr, this.attrs.get(this.coldattr) - intensity);
            }
        }
    }
*/
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
            return baseEfficiency + Math.min(maxBoost, boostScale * attrsum) - attrs.get(coldattr);
        }

    }
}