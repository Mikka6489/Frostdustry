package frostdustry.world.blocks.defense.turrets;

import mindustry.world.blocks.defense.turrets.*;

import frostdustry.logic.*;
import frostdustry.type.*;

public class FrostITurret extends ItemTurret{
    public float recievedHeat = 0f;
    FrostMethods FrostMethods = new FrostMethods();

    public FrostITurret(String name){
        super(name);
    }

    public class FrostITurretBuild extends ItemTurretBuild implements HeatReciever{
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