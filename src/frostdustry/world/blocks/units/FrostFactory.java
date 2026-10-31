package frostdustry.world.blocks.units;

import arc.*;
import arc.util.*;
import arc.math.*;
import mindustry.Vars;
import mindustry.game.EventType.*;
import mindustry.gen.*;
import mindustry.world.blocks.payloads.*;
import mindustry.world.blocks.units.*;

import frostdustry.logic.*;
import frostdustry.type.*;

public class FrostFactory extends UnitFactory{
    public FrostFactory(String name){
        super(name);
    }
    
    public class FrostFactoryBuild extends UnitFactoryBuild implements HeatReciever{
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
	    
        }

        @Override
        public void draw(){
            super.draw();
            if (FrostMethods.calcCold(recievedHeat) > 3f) {
                FrostMethods.drawStoppedOverlay(this);
            }
        }

        @Override
        public void updateTile(){
            if(!configurable){
                currentPlan = 0;
            }

            if(currentPlan < 0 || currentPlan >= plans.size){
                currentPlan = -1;
            }

            if(efficiency > 0 && currentPlan != -1){
                time += edelta() * speedScl * Vars.state.rules.unitBuildSpeed(team) / (FrostMethods.calcCold(recievedHeat) + 1f);
                progress += edelta() * Vars.state.rules.unitBuildSpeed(team) / (FrostMethods.calcCold(recievedHeat) + 1f);
                speedScl = Mathf.lerpDelta(speedScl, 1f, 0.05f);
            }else{
                speedScl = Mathf.lerpDelta(speedScl, 0f, 0.05f);
            }

            moveOutPayload();

            if(currentPlan != -1 && payload == null){
                UnitPlan plan = plans.get(currentPlan);

                //make sure to reset plan when the unit got banned after placement
                if(plan.unit.isBanned()){
                    currentPlan = -1;
                    return;
                }

                if(progress >= plan.time){
                    progress %= 1f;

                    Unit unit = plan.unit.create(team);
                    if(unit.isCommandable()){
                        if(commandPos != null){
                            unit.command().commandPosition(commandPos);
                        }

                        unit.command().command(command == null && unit.type.defaultCommand != null ? unit.type.defaultCommand : command);
                    }

                    createSound.at(this, 1f + Mathf.range(0.06f), createSoundVolume);
                    payload = new UnitPayload(unit);
                    payVector.setZero();
                    consume();
                    Events.fire(new UnitCreateEvent(payload.unit, this));
                }

                progress = Mathf.clamp(progress, 0, plan.time);
            }else{
                progress = 0f;
            }
        }
    }
}
