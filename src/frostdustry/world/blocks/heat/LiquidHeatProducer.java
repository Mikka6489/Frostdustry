package frostdustry.world.blocks.heat;

import mindustry.*;
import mindustry.ctype.*;
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
        Liquid diesel = Vars.content.getByName(ContentType.liquid, "frostdustry-diesel");
        consume(new ConsumeLiquidFilter(liquid -> liquid instanceof FrostLiquid && liquid != diesel, 0.357f) {
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
