package frostdustry.world.blocks.drills;

import arc.util.*;
import arc.math.*;
import mindustry.type.*;
import mindustry.world.blocks.environment.Floor;
import mindustry.world.blocks.production.*;
import mindustry.world.*;
import mindustry.world.meta.*;

import frostdustry.logic.*;
import frostdustry.type.*;
import frostdustry.world.blocks.environment.*;

import static mindustry.Vars.*;

public class FrostDrill extends Drill implements HeatReciever{
    public float recievedHeat = 0f;
    FrostMethods FrostMethods = new FrostMethods();

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

    public void recieveHeat(float heat) {
        recievedHeat = heat;
    }

    public boolean canBeHeated() {
        return true;
    }

    @Override 
    public float getDrillTime(Item item){
        return super.getDrillTime(item) * (FrostMethods.calcCold(recievedHeat) * 2f + 1f);
    }

    public class FrostDrillBuild extends DrillBuild {
	//float delay = 10; 
	@Override
        public void updateTile(){
            if(timer(timerDump, dumpTime / timeScale)){
                dump(dominantItem != null && items.has(dominantItem) ? dominantItem : null);
            }

            if(dominantItem == null){
                return;
            }

            timeDrilled += warmup * delta();

            float delay = getDrillTime(dominantItem); 

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
		//delay = getDrillTime(dominantItem);
		FrostMethods.logUsefulShit(recievedHeat);
                int amount = (int)(progress / delay);
                for(int i = 0; i < amount; i++){
                    offload(dominantItem);
                }
		        //Log.info("Drill progress: @, delay: @, dominantItems: @, itemCapacity: @, items.total(): @", progress, delay, dominantItems, itemCapacity, items.total());

                progress %= delay;

                if(wasVisible && Mathf.chanceDelta(drillEffectChance * warmup)) drillEffect.at(x + Mathf.range(drillEffectRnd), y + Mathf.range(drillEffectRnd), dominantItem.color);
            }
        }
    }
}
