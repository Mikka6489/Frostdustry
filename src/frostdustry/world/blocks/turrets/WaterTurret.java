package frostdustry.world.blocks.turrets;

import arc.util.*;
import mindustry.world.blocks.defense.turrets.*;

import frostdustry.logic.*;
import frostdustry.type.*;

public class WaterTurret extends LiquidTurret{

    public WaterTurret(String name){
        super(name);
    }

    public class WaterTurretBuild extends LiquidTurretBuild implements HeatReciever{
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
            if (baseReloadSpeed() < -1f) {
                efficiency = 0f;
            }
        }

        @Override
        public void draw(){
            super.draw();
            if (baseReloadSpeed() < -1f) {
                FrostMethods.drawStoppedOverlay(this);
            }
        }

        @Override
        protected float baseReloadSpeed(){
            return super.baseReloadSpeed() - (FrostMethods.calcCold(recievedHeat) / 2);
        }
    }
}
