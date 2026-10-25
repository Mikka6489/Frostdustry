package frostdustry.world.blocks.power;

import mindustry.*;
import mindustry.content.*;
import mindustry.ctype.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.blocks.power.*;
import mindustry.world.consumers.*;

import frostdustry.logic.*;
import frostdustry.type.*;

public class FuelGenerator extends ConsumeGenerator{
    public FuelGenerator(String name){
        super(name);
    }
/*
    @Override
    public void init(){
        Liquid diesel = Vars.content.getByName(ContentType.liquid, "frostdustry-diesel");
        consume(new ConsumeLiquidFilter(liquid -> liquid == diesel, 0.357f) {
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
*/
    public class FuelGeneratorBuild extends ConsumeGeneratorBuild implements HeatReciever{
        FrostMethods FrostMethods = new FrostMethods();
        public float recievedHeat = 0;

        @Override
        public void recieveHeat(float heat) {
            recievedHeat = heat;
        }

        @Override
        public boolean canBeHeated() {
            return true;
        }
//TODO: change this so power making drops by cold
        @Override
        public float getProgressIncrease(float base){
            return super.getProgressIncrease(base) / (FrostMethods.calcCold(recievedHeat) + 1f);
        }
/*
        @Override
        public boolean acceptLiquid(Building source, Liquid liquid){
            if((liquid != Liquids.oil && !(liquid instanceof FrostLiquid)) || !super.acceptLiquid(source, liquid)){
                return false;
            }

            for(Liquid stored : Vars.content.liquids()){
                if(stored != liquid && liquids.get(stored) > 0.001f){
                    return false;
                }
            }
            return true;
        }*/
    }
}
