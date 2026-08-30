package frostdustry.world;

import arc.*;
import arc.math.*;
import arc.util.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.ui.*;
import mindustry.world.*;
import mindustry.world.meta.*;
import mindustry.world.blocks.*;
import arc.*;
import mindustry.game.EventType.*;

import frostdustry.content.*;
import frostdustry.world.meta.*;
import frostdustry.logic.*;

public class FrostBlock extends Block{

	public Attribute coldattr = FrostVars.coldattr;
    public float cold = FrostVars.cold;
    public Attributes attrs = new Attributes();
    public Attribute attribute = Attribute.heat;
    public boolean canBeHeated = true;
    public float baseEfficiency = 1f;
    public float boostScale = 1f;
    public float maxBoost = 1f;
    public float minEfficiency = -1f;
    public float displayEfficiencyScale = 1f;
    public float useTime = 400f;
    public boolean displayEfficiency = true;
    public boolean scaleLiquidConsumption = false;

    public FrostBlock(String name){
        super(name);
    }

    @Override
    public void setStats(){
        super.setStats();
        if (canBeHeated) {
            stats.add(FrostStat.cold, (Mathf.round(coldattr.env()) * 10) + "°C");
        }
    }

    @Override
    public void setBars(){
        super.setBars();
        if(!displayEfficiency) return;
        if (canBeHeated) {
            addBar("Heat", (FrostBuilding entity) ->
                new Bar(
                () -> Core.bundle.format("bar.cold", (int)(entity.efficiencyMultiplier() * 10 * displayEfficiencyScale)),
                () -> Pal.techBlue,
                entity::efficiencyMultiplier));
        }
    }

    public class FrostBuilding extends Building {
        public float attrsum, useProgress;

        @Override
        public float getProgressIncrease(float base){
            return super.getProgressIncrease(base) * efficiencyMultiplier();
        }

        @Override
        public float efficiencyScale(){
            return scaleLiquidConsumption ? efficiencyMultiplier() : super.efficiencyScale();
        }

        public float efficiencyMultiplier(){
//		Log.info("efficiencyMultiplier of: " + (this.block) + ": " + (efficiencyMultiplier()));
    		return (baseEfficiency + Math.min(maxBoost, boostScale * attrsum) + attribute.env()) - coldattr.env();
        }
}
}
