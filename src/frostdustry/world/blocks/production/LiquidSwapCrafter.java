package frostdustry.world.blocks.production;

import arc.math.*;
import arc.util.*;
import mindustry.world.blocks.production.*;
import mindustry.type.*;
import frostdustry.type.*;

public class LiquidSwapCrafter extends HeatCrafter{
    public LiquidSwapCrafter(String name){
        super(name);
    }

    @Override
    public void setBars(){
        super.setBars();

        if(outputLiquids != null){
            for(LiquidStack output : outputLiquids){
                removeBar("liquid-" + output.liquid.name);
            }
        }

        addLiquidBar((HeatSwapBuild build) -> {
            LiquidStack output = build.selectedOutput();
            return output == null ? null : output.liquid;
        });
    }

    public class HeatSwapBuild extends HeatCrafterBuild{
        @Override
        public void updateTile(){
            heat = calculateHeat(sideHeat);
            LiquidStack output = selectedOutput();

            if(efficiency > 0f && output != null){
                progress += getProgressIncrease(craftTime);
                warmup = Mathf.approachDelta(warmup, warmupTarget(), warmupSpeed);

                float liquidIncrease = getProgressIncrease(1f);
                float available = Math.max(liquidCapacity - liquids.get(output.liquid), 0f);
                handleLiquid(this, output.liquid, Math.min(output.amount * liquidIncrease, available));

                if(wasVisible && Mathf.chanceDelta(updateEffectChance)){
                    updateEffect.at(x + Mathf.range(size * updateEffectSpread), y + Mathf.range(size * updateEffectSpread));
                }
            }else{
                warmup = Mathf.approachDelta(warmup, 0f, warmupSpeed);
            }

            totalProgress += warmup * Time.delta;

            if(output != null && progress >= 1f){
                craft();
            }

            if(outputItems != null && timer(timerDump, dumpTime / timeScale)){
                for(ItemStack item : outputItems){
                    dump(item.item);
                }
            }

            if(output != null){
                dumpLiquid(output.liquid, 2f);
            }
        }

        private LiquidStack selectedOutput(){
            for(LiquidStack output : LiquidSwapCrafter.this.outputLiquids){
                FrostLiquid liquid = (FrostLiquid)output.liquid;
                if(heat >= liquid.minHeatingRequired && heat <= liquid.maxHeatingRequired){
                    return output;
                }
            }
            return null;
        }
    }
}
