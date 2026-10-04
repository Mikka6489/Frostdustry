package frostdustry.world.blocks.turrets;

import mindustry.world.blocks.defense.turrets.*;

import frostdustry.logic.*;
import frostdustry.type.*;

public class ConductiveTurret extends PowerTurret{
    public float recievedHeat = 0f;
    FrostMethods FrostMethods = new FrostMethods();

    public ConductiveTurret(String name){
        super(name);
    }

    public class ConductiveTurretBuild extends PowerTurretBuild implements HeatReciever{
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