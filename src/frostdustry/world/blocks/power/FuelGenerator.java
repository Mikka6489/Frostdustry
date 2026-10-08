package frostdustry.world.blocks.power;

import mindustry.game.Team;
import mindustry.Vars;
import mindustry.content.Liquids;
import mindustry.gen.Building;
import mindustry.type.*;
import mindustry.world.Tile;
import mindustry.world.blocks.power.*;
import mindustry.world.consumers.ConsumeLiquidFilter;
import frostdustry.logic.*;
import frostdustry.type.FrostLiquid;

public class FuelGenerator extends ConsumeGenerator{
    public int cylinderTier = 1;

    public FuelGenerator(String name){
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

    @Override
    public boolean canPlaceOn(Tile tile, Team team, int rotation){
        if(tile == null) return false;
        boolean allowedByExistingRules = cylinderTier == 1 && FrostMethods.runningGenerators < 4
            || tile.block() instanceof FuelGenerator && FrostMethods.fuelGenPlaced;
        return allowedByExistingRules && isAdjacentToGeneratorOrFuelGenerator(tile);
    }

    public boolean isAdjacentToGeneratorOrFuelGenerator(Tile tile){
        return tile != null && (isGeneratorOrFuelGeneratorAt(tile.x + 1, tile.y)
            || isGeneratorOrFuelGeneratorAt(tile.x - 1, tile.y)
            || isGeneratorOrFuelGeneratorAt(tile.x, tile.y + 1)
            || isGeneratorOrFuelGeneratorAt(tile.x, tile.y - 1));
    }

    private boolean isGeneratorOrFuelGeneratorAt(int x, int y){
        Tile nearby = Vars.world.tile(x, y);
        return nearby != null && (nearby.block() instanceof frostdustry.world.blocks.defense.Generator
            || nearby.block() instanceof FuelGenerator);
    }

    public class FuelGeneratorBuild extends ConsumeGeneratorBuild{
        @Override
        public void created(){
            super.created();
            if (cylinderTier == 1) FrostMethods.runningGenerators++;
            if (cylinderTier > 1) FrostMethods.runningGenerators += 99;
            FrostMethods.fuelGenPlaced = true;
        }

        @Override
        public void onRemoved(){
            super.onRemoved();
            if (cylinderTier == 1) FrostMethods.runningGenerators--;
            if (cylinderTier > 1) FrostMethods.runningGenerators = 0;
            if (FrostMethods.runningGenerators == 0) FrostMethods.fuelGenPlaced = false;
        }

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
        }
    }
}
