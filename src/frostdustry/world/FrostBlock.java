package frostdustry.world;

import arc.*;
import arc.util.Log;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.ui.*;
import mindustry.world.*;
import mindustry.world.meta.*;
import mindustry.world.blocks.*;
import java.util.concurrent.TimeUnit;

import frostdustry.content.*;
import frostdustry.world.meta.*;

public class FrostBlock extends Block{
    @Deprecated
    
    public final Attribute coldattr = FrostAttribute.cold;
    public Attributes attrs = new Attributes();


    private transient float timeScale = 1f, timeScaleDuration;

    public boolean heated = false;
    public boolean being_heated = false;



    public float baseEfficiency = 1f;
    public float boostScale = 1f;
    public float maxBoost = 1f;
    public float minEfficiency = -1f;
    public float displayEfficiencyScale = 1f;
    public boolean displayEfficiency = true;
    public boolean scaleLiquidConsumption = false;

    

    public FrostBlock(String name){
        super(name);
        attrs.set(coldattr, 0);


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

        Log.info("applyHeat triggered");

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
            return baseEfficiency + Math.min(maxBoost, boostScale * attrsum) - attrs.get(coldattr);
        }

    }
}
