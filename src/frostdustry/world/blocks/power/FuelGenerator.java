package frostdustry.world.blocks.power;

import arc.util.*;
import mindustry.world.blocks.power.*;

import frostdustry.logic.*;
import frostdustry.type.*;

public class FuelGenerator extends ConsumeGenerator{
    public FuelGenerator(String name){
        super(name);
    }

    public class FuelGeneratorBuild extends ConsumeGeneratorBuild implements HeatReciever{
        FrostMethods FrostMethods = new FrostMethods();
        public float recievedHeat, heatDuration;

        @Override
        public void recieveHeat(float heat, float duration) {
            if (heat >= recievedHeat - 0.001F) {
                heatDuration = Math.max(heatDuration, duration);
            }

            recievedHeat = heat;
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
            if ((FrostMethods.calcCold(recievedHeat) + 1f) > 4f) {
                efficiency = 0f;
            }
        }

        @Override
        public void draw(){
            super.draw();
            if ((FrostMethods.calcCold(recievedHeat) + 1f) > 4f) {
                FrostMethods.drawStoppedOverlay(this);
            }
        }

        @Override
        public float getPowerProduction(){
            return super.getPowerProduction() / (FrostMethods.calcCold(recievedHeat) + 1f);
        }
    }
}
