package frostdustry.world.blocks.units;

import arc.*;
import arc.util.*;
import arc.math.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.game.EventType.*;
import mindustry.world.blocks.units.*;

import static mindustry.Vars.*;

import frostdustry.logic.*;
import frostdustry.type.*;

public class FrostReconstructor extends Reconstructor{
    public FrostReconstructor(String name){
        super(name);
    }
    
    public class FrostReconstructorBuild extends ReconstructorBuild implements HeatReciever{
        boolean constructing;
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
            //cache value to prevent repeated calls and multithreading issues
            constructing = constructing();
            boolean valid = false;

            if(payload != null){
                //check if offloading
                if(!hasUpgrade(payload.unit.type)){
                    moveOutPayload();
                }else{ //update progress
                    if(moveInPayload()){
                        if(efficiency > 0){
                            valid = true;
                            progress += edelta() * state.rules.unitBuildSpeed(team) / (FrostMethods.calcCold(recievedHeat) + 1f);
                        }

                        //upgrade the unit
                        if(progress >= constructTime){
                            payload.unit = upgrade(payload.unit.type).create(payload.unit.team());

                            if(payload.unit.isCommandable()){
                                if(commandPos != null){
                                    payload.unit.command().commandPosition(commandPos);
                                }
                                //this already checks if it is a valid command for the unit type
                                payload.unit.command().command(command == null && payload.unit.type.defaultCommand != null ? payload.unit.type.defaultCommand : command);
                            }

                            createSound.at(this, 1f + Mathf.range(0.06f), createSoundVolume);
                            progress %= 1f;
                            Effect.shake(2f, 3f, this);
                            Fx.producesmoke.at(this);
                            consume();
                            Events.fire(new UnitCreateEvent(payload.unit, this));
                        }
                    }
                }
            }

            speedScl = Mathf.lerpDelta(speedScl, Mathf.num(valid), 0.05f);
            time += edelta() * speedScl * state.rules.unitBuildSpeed(team) / (FrostMethods.calcCold(recievedHeat) + 1f);
        }
    }
}
