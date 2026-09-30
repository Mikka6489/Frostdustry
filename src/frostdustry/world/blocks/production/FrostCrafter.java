package frostdustry.world.blocks.production;

import mindustry.world.blocks.production.*;

import frostdustry.type.*;
import frostdustry.logic.*;

public class FrostCrafter extends GenericCrafter{
    public float recievedHeat = 0f;

    public FrostCrafter(String name){
        super(name);
    }

    public class FrostCrafterBuild extends GenericCrafterBuild implements HeatReciever{
        FrostMethods FrostMethods = new FrostMethods();

        @Override
        public void recieveHeat(float heat) {
            recievedHeat = heat;
        }

        @Override
        public boolean canBeHeated() {
            return true;
        }

        @Override
        public float getProgressIncrease(float base){
            return super.getProgressIncrease(base) / (FrostMethods.calcCold(recievedHeat) + 1f);
        }
    }
}