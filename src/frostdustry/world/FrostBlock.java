package frostdustry.world;

import arc.*;
import arc.util.*;
import arc.math.*;
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

    private transient float timeScale = 1f, timeScaleDuration;

    public boolean canBeHeated = true;

    public float reload = 60f;

    public float baseEfficiency = 1f;
    public float boostScale = 1f;
    public float maxBoost = 1f;
    public float minEfficiency = -1f;
    public float displayEfficiencyScale = 1f;
    public boolean displayEfficiency = true;
    public boolean scaleLiquidConsumption = false;

    

    public FrostBlock(String name){
        super(name);
        update = true;
        this.attrs.set(this.coldattr, 0);
    }

    @Override
    public void setStats(){
        super.setStats();
        stats.add(FrostStat.cold, (this.attrs.get(this.coldattr) * 10) + "°C");
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
//        Log.info("voodoo magic before | timescale: " + this.timeScale + " | timeScaleDuration: " + this.timeScaleDuration + " | intensity: " + intensity + " | duration: " + duration);
        if (intensity >= this.timeScale - 0.001F) {
            this.timeScaleDuration = Math.max(this.timeScaleDuration, duration);
        }
        this.timeScale = Math.max(this.timeScale, intensity);
//        Log.info("current cold of: " + this + " before change: " + this.attrs.get(this.coldattr));
        this.attrs.set(this.coldattr, attrs.get(this.coldattr) - intensity);
//        Log.info("current cold of: " + this + " after: " + this.attrs.get(this.coldattr));
//        Log.info("voodoo magic after | timeScale: " + this.timeScale + " | timeScaleDuartion: " + this.timeScaleDuration + " | intensity: " + intensity + " | duration: " + duration);
//        Log.info("current cold of: " + this + " after: " + this.coldattr.env());
    }    

    public class FrostBuilding extends Building {
        public float attrsum;
        public float what, charge = Mathf.random(reload);
        public float indextimer = 0;

        public void updateTile(){
            charge += 1 * Time.delta;

//            Log.info("again");
//            Log.info("charge: " + charge);
//            Log.info("reload: " + reload);

            if (charge >= reload) {
                indextimer += 1;
                Log.info("timer is at: " + indextimer);
                charge = 0f;
                if (indextimer > 60){
                    indextimer = 0;
                };
            };
        }

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
