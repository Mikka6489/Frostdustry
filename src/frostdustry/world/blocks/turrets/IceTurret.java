package frostdustry.world.blocks.turrets;

import mindustry.world.blocks.defense.turrets.*;

import frostdustry.logic.*;
import frostdustry.type.*;

public class IceTurret extends ItemTurret{
    public float recievedHeat = 0f;
    FrostMethods FrostMethods = new FrostMethods();

    public IceTurret(String name){
        super(name);
    }

    public class IceTurretBuild extends ItemTurretBuild implements HeatReciever{
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