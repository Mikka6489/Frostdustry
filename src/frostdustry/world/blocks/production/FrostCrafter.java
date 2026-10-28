package frostdustry.world.blocks.production;

import arc.*;
import arc.math.*;
import arc.util.*;
import mindustry.graphics.*;
import mindustry.ui.*;
import mindustry.world.blocks.production.*;

import frostdustry.type.*;
import frostdustry.logic.*;

public class FrostCrafter extends GenericCrafter{
    public FrostCrafter(String name){
        super(name);
    }

        @Override
    public void setBars(){
        super.setBars();
        addBar("craftspeed", (FrostCrafterBuild e) -> {
            return new Bar(
                () -> Core.bundle.format(
                    "bar.craftspeed", 
                    Strings.fixed(craftTime / 60f * e.timeScale(), 2),
                    Strings.fixed(e.displayCraftTime / 60f * e.timeScale(), 2)
                ),
                () -> Pal.ammo,
                () -> e.warmup
            );
        });
    }

    public class FrostCrafterBuild extends GenericCrafterBuild implements HeatReciever{
        FrostMethods FrostMethods = new FrostMethods();
        public float recievedHeat, heatDuration, lastCraftTime, displayCraftTime;

        @Override
        public void recieveHeat(float heat, float duration) {
            if (heat >= recievedHeat - 0.001F) {
                heatDuration = Math.max(heatDuration, duration);
            }

            recievedHeat = Math.max(recievedHeat, heat);
        }

        @Override
        public boolean canBeHeated() {
            return true;
        }

        @Override
        public void update(){
            super.update();
            if ((heatDuration -= Time.delta) <= 0.0F || !canBeHeated()) {
                recievedHeat = 0F;
            }
        }
	@Override
	public void draw(){
		super.draw();
		if (FrostMethods.calcCold(recievedHeat) > 2.5f) {// lastCraftTime < 12f
			FrostMethods.drawStoppedOverlay(this);
		}
	}
        @Override
        public void updateTile(){
            if(efficiency > 0){

                progress += getProgressIncrease(craftTime) / (FrostMethods.calcCold(recievedHeat) + 1f);
                warmup = Mathf.approachDelta(warmup, warmupTarget(), warmupSpeed);

                displayCraftTime = Mathf.approachDelta(displayCraftTime, lastCraftTime, 2f);
                lastCraftTime = craftTime / (FrostMethods.calcCold(recievedHeat) +1f);

                if (FrostMethods.calcCold(recievedHeat) > 2.5f) efficiency = 0f;

                //continuously output based on efficiency
                if(outputLiquids != null){
                    float inc = getProgressIncrease(1f);
                    for(var output : outputLiquids){
                        handleLiquid(this, output.liquid, Math.min(output.amount * inc, liquidCapacity - liquids.get(output.liquid)));
                    }
                }

                if(wasVisible && Mathf.chanceDelta(updateEffectChance)){
                    updateEffect.at(x + Mathf.range(size * updateEffectSpread), y + Mathf.range(size * updateEffectSpread));
                }
            }else{
                warmup = Mathf.approachDelta(warmup, 0f, warmupSpeed);
            }

            if (lastCraftTime > 0.01f) efficiency = 0f;

            //TODO may look bad, revert to edelta() if so
            totalProgress += warmup * Time.delta;

            if(progress >= 1f){
//                Log.info("calcCold: @, craftTime: @, last: @, display: @", FrostMethods.calcCold(recievedHeat), craftTime, lastCraftTime, displayCraftTime);
                //recievedHeat = 0f;
                craft();
            }

            dumpOutputs();
        }
    }
}
