package frostdustry.world.blocks.power;

import mindustry.game.Team;
import mindustry.world.Tile;
import mindustry.world.blocks.power.*;

import frostdustry.logic.*;

public class FuelGenerator extends ConsumeGenerator{
    public int cylinderTier = 1;

    public FuelGenerator(String name){
        super(name);
    }

    @Override
    public boolean canPlaceOn(Tile tile, Team team, int rotation){
        if(tile == null) return false;
        if (cylinderTier == 1 && FrostMethods.runningGenerators < 4) return true;
        return tile.block() instanceof FuelGenerator && FrostMethods.fuelGenPlaced;
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
    }
}
