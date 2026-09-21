package frostdustry.world.blocks.production;

import mindustry.world.blocks.production.*;
import arc.util.*;

import frostdustry.type.*;
import frostdustry.logic.*;

public class FrostGCrafter extends GenericCrafter{
    public float recievedHeat = 0.0001f;

    public FrostGCrafter(String name){
        super(name);
    }

    public class FrostGCrafterBuild extends GenericCrafterBuild implements HeatReciever{
        FrostMethods FrostMethods = new FrostMethods();

        @Override
        public void recieveHeat(float heat) {
            Log.info("Recieved heat: @", heat);
            recievedHeat = heat;
        }

        @Override
        public boolean canBeHeated() {
            return true;
        }

        @Override
        public float getProgressIncrease(float base){
//            Log.info(super.getProgressIncrease(base) / (FrostMethods.calcCold(recievedHeat) + 0.01f));
//            Log.info("Recieved heat: @, Cold env: @, Calc cold: @", recievedHeat, FrostMethods.cold.env(), FrostMethods.calcCold(recievedHeat));
            return super.getProgressIncrease(base) / (FrostMethods.calcCold(recievedHeat) + 1f);
        }
    }
}