package frostdustry.world.blocks.heat;

import mindustry.content.*;
import mindustry.type.*;
import mindustry.world.blocks.heat.*;
import mindustry.world.consumers.*;

import frostdustry.type.*;

public class LiquidHeatProducer extends HeatProducer {
    public LiquidHeatProducer(String name){
        super(name);
    }
        @Override
    public void init(){
        consume(new ConsumeLiquidFilter(liquid -> liquid == Liquids.oil || liquid instanceof FrostLiquid, 0.357f) {
            {
                multiplier = build -> {
                    Liquid fuel = getConsumed(build);
                    return fuel instanceof FrostLiquid ? ((FrostLiquid)fuel).usageMultiplier : 1f;
                };
            }

            @Override
            public float liquidEfficiencyMultiplier(Liquid liquid){
                return liquid instanceof FrostLiquid
                    ? ((FrostLiquid)liquid).powerMultiplier
                    : 1f;
            }
        });
        super.init();
    }
    
}
