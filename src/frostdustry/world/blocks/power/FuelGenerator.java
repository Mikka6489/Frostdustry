package frostdustry.world.blocks.power;

import arc.*;
import arc.util.*;
import mindustry.graphics.*;
import mindustry.ui.*;
import mindustry.world.blocks.power.*;

import frostdustry.logic.*;
import frostdustry.type.*;

public class FuelGenerator extends ConsumeGenerator{
    public FuelGenerator(String name){
        super(name);
    }

    @Override
    public void setBars(){
        super.setBars();
	    removeBar("power");
        addBar("coldpower", (FuelGeneratorBuild e) -> {
            return new Bar(
                () -> Core.bundle.format(
                    "bar.coldpower", 
                    Strings.fixed(e.getBasePowerProduction() * 60f * e.timeScale(), 1),
                    Strings.fixed(e.getPowerProduction() * 60f * e.timeScale(), 1)
                ),
                () -> Pal.ammo,
                () -> e.warmup
            );
        });
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
            if (getPowerProduction() < (getBasePowerProduction() / 3)) {
                efficiency = 0f;
            }
        }

        @Override
        public void draw(){
            super.draw();
            if (getPowerProduction() < (getBasePowerProduction() / 3)) {
                FrostMethods.drawStoppedOverlay(this);
            }
        }

        public float getBasePowerProduction(){
            return super.getPowerProduction();
        }

        @Override
        public float getPowerProduction(){
            return getBasePowerProduction() / (FrostMethods.calcCold(recievedHeat) + 1f);
        }
    }
}
