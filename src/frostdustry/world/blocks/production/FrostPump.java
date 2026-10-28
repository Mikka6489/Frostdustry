package frostdustry.world.blocks.production;

import arc.util.*;
import mindustry.world.blocks.production.*;

import frostdustry.logic.*;
import frostdustry.type.*;

public class FrostPump extends Pump{

    public FrostPump(String name){
        super(name);
    }

    public class FrostPumpBuild extends PumpBuild implements HeatReciever{
        FrostMethods FrostMethods = new FrostMethods();
        public float recievedHeat, heatDuration;

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
            if ((FrostMethods.calcCold(recievedHeat) + 1f) > 3f) {
                efficiency = 0f;
            }
	    Log.info(pumpAmount);
	    //Log.info(totalProgress());
        }

        @Override
        public void draw(){
            super.draw();
            if ((FrostMethods.calcCold(recievedHeat) + 1f) > 3f) {
                FrostMethods.drawStoppedOverlay(this);
            }
        }

        @Override
        public float getProgressIncrease(float base){
            return super.getProgressIncrease(base) / (FrostMethods.calcCold(recievedHeat) + 1f);
        }
    }
}
