package frostdustry.world;

import arc.*;
import arc.math.*;
//import arc.util.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.ui.*;
import mindustry.world.*;
import mindustry.world.meta.*;
import mindustry.world.blocks.*;
import frostdustry.world.meta.*;
import frostdustry.logic.*;

public class FrostBlock extends Block{

	public Attribute cold = FrostVars.cold;
    public Attributes attrs = new Attributes();
    public Attribute attribute = Attribute.heat;
    public boolean canUseAttributes = false;
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
            stats.add(FrostStat.cold, (Mathf.round(cold.env()) * 10) + "°C");
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
        public float attrsum, warmup;

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
    		return (baseEfficiency + Math.min(maxBoost, boostScale * attrsum) + attribute.env()) - cold.env();
        }
	
	@Override
	public void pickedUp(){
		attrsum = 0f;
		warmup = 0f;	
	}

	@Override
	public void onProximityUpdate(){
        if (canUseAttributes){
            super.onProximityUpdate();

            attrsum = sumAttribute(attribute, tile.x,tile.y); 	
        }	
	} 
    
    
    }
}
