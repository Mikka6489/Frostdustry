package frostdustry.world.blocks.turrets;

import mindustry.world.blocks.defense.turrets.*;

import frostdustry.logic.*;
import frostdustry.type.*;

public class WaterTurret extends LiquidTurret{
    public float recievedHeat = 0f;
    FrostMethods FrostMethods = new FrostMethods();

    public WaterTurret(String name){
        super(name);
    }

    public class WaterTurretBuild extends LiquidTurretBuild implements HeatReciever{
        @Override
        public void recieveHeat(float heat) {
            recievedHeat = heat;
        }

        @Override
        public boolean canBeHeated() {
            return true;
        }

        @Override
        protected float baseReloadSpeed(){
            return super.baseReloadSpeed() - (FrostMethods.calcCold(recievedHeat));
        }
    }
}