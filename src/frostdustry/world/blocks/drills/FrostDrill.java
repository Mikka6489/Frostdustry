package frostdustry.world.blocks.drills;

import arc.util.*;
import arc.math.*;
import mindustry.world.blocks.environment.*;
import mindustry.world.blocks.production.*;
import mindustry.world.*;
import mindustry.world.meta.*;

import frostdustry.logic.*;
import frostdustry.type.*;
import frostdustry.world.blocks.environment.*;

import static mindustry.Vars.*;

public class FrostDrill extends Drill {    

    public FrostDrill(String name) {
        super(name);
    }

    @Override
    public boolean canMine(Tile tile){
        return tile != null && !(tile.overlay() instanceof LimitedOre) && super.canMine(tile);
    }

    @Override
    public void setStats(){
        super.setStats();

        stats.replace(Stat.drillTier, StatValues.drillables(drillTime, hardnessDrillMultiplier, size * size, drillMultipliers, block ->
            block instanceof Floor floor && !(floor instanceof LimitedOre) && !floor.wallOre && floor.itemDrop != null &&
            floor.itemDrop.hardness <= tier && (blockedItems == null || !blockedItems.contains(floor.itemDrop)) &&
            (indexer.isBlockPresent(floor) || state.isMenu())
        ));
    }

    public class FrostDrillBuild extends DrillBuild implements HeatReciever{
        public float recievedHeat, heatDuration;
        FrostMethods FrostMethods = new FrostMethods();

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
        }

	    @Override
        public void updateTile(){
            if(timer(timerDump, dumpTime / timeScale)){
                dump(dominantItem != null && items.has(dominantItem) ? dominantItem : null);
            }

            if(dominantItem == null){
                return;
            }
            timeDrilled += warmup * delta();

            float delay = getDrillTime(dominantItem) * (FrostMethods.calcCold(recievedHeat) * 2f + 1f); 

	        if (delay > 2500f) efficiency = 0;

            if(items.total() < itemCapacity && dominantItems > 0 && efficiency > 0){
                float speed = Mathf.lerp(1f, liquidBoostIntensity, optionalEfficiency) * efficiency;

                lastDrillSpeed = (speed * dominantItems * warmup) / delay;
                warmup = Mathf.approachDelta(warmup, speed, warmupSpeed);
                progress += delta() * dominantItems * speed * warmup;

                if(Mathf.chanceDelta(updateEffectChance * warmup))
                    updateEffect.at(x + Mathf.range(size * 2f), y + Mathf.range(size * 2f));
            }else{
                lastDrillSpeed = 0f;
                warmup = Mathf.approachDelta(warmup, 0f, warmupSpeed);
                return;
            }

            if(dominantItems > 0 && progress >= delay && items.total() < itemCapacity){
                int amount = (int)(progress / delay);
                for(int i = 0; i < amount; i++){
                    offload(dominantItem);
                }

                progress %= delay;

                if(wasVisible && Mathf.chanceDelta(drillEffectChance * warmup)) drillEffect.at(x + Mathf.range(drillEffectRnd), y + Mathf.range(drillEffectRnd), dominantItem.color);
            }
        }
    }
}
