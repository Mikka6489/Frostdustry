package frostdustry.world.blocks.production;

import mindustry.world.blocks.production.*;

import frostdustry.logic.*;
import frostdustry.type.*;

public class FrostPump extends Pump{
    public float recievedHeat = 0f;

    public FrostPump(String name){
        super(name);
    }

    public class FrostPumpBuild extends PumpBuild implements HeatReciever{
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
