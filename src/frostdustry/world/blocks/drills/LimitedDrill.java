package frostdustry.world.blocks.drills;

import arc.util.*;
import arc.math.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.world.blocks.production.*;
import mindustry.*;
import mindustry.graphics.*;
import mindustry.world.*;
import mindustry.world.meta.*;

import frostdustry.logic.*;
import frostdustry.type.*;
import frostdustry.world.blocks.environment.*;

import static mindustry.Vars.*;

public class LimitedDrill extends Drill implements HeatReciever{
    public float recievedHeat = 0f;
    FrostMethods FrostMethods = new FrostMethods();

    public LimitedDrill(String name) {
        super(name);
    }

    @Override
    public boolean canMine(Tile tile){
        return tile != null && tile.overlay() instanceof LimitedOre && super.canMine(tile);
    }

    @Override
    public void setStats(){
        super.setStats();

        stats.replace(Stat.drillTier, StatValues.drillables(drillTime, hardnessDrillMultiplier, size * size, drillMultipliers, block ->
            block instanceof LimitedOre ore && !ore.wallOre && ore.itemDrop != null &&
            ore.itemDrop.hardness <= tier && (blockedItems == null || !blockedItems.contains(ore.itemDrop)) &&
            (indexer.isBlockPresent(ore) || state.isMenu())
        ));
    }

    public void recieveHeat(float heat) {
        recievedHeat = heat;
    }

    @Override
    public void setBars(){
        super.setBars();
        addBar("orecount", entity -> {
            LimitedDrillBuild build = (LimitedDrillBuild) entity;
            return new Bar(
                () -> "Ore: " + build.totalOreCount(),
                () -> Pal.techBlue,
                () -> Mathf.clamp(build.totalOreCount() / (float)(size * size * 20), 0f, 1f)
            );
        });
    }

    public boolean canBeHeated() {
        return true;
    }

    @Override 
    public float getDrillTime(Item item){
        if (super.getDrillTime(item) * (FrostMethods.calcCold(recievedHeat) * 2f + 1f) > 2500f) {
            return 99999;
        } else{
            return super.getDrillTime(item) * (FrostMethods.calcCold(recievedHeat) * 2f + 1f);
        }
//        return super.getDrillTime(item) * (FrostMethods.calcCold(recievedHeat) * 2f + 1f);
    }

    public class LimitedDrillBuild extends DrillBuild{
        public int totalOreCount(){
            int total = 0;
            for(int dx = 0; dx < size; dx++){
                for(int dy = 0; dy < size; dy++){
                    Tile oreTile = Vars.world.tile(tile.x + dx, tile.y + dy);
                    if(oreTile == null || !(oreTile.overlay() instanceof LimitedOre frostOre)) continue;
                    total += frostOre.getOreCount(oreTile);
                }
            }
            return total;
        }

        @Override
        public void updateTile(){
            if(timer(timerDump, dumpTime / timeScale)){
                dump(dominantItem != null && items.has(dominantItem) ? dominantItem : null);
            }

            if(dominantItem == null){
                return;
            }

            Item drillItem = dominantItem;
            timeDrilled += warmup * delta();

            float delay = getDrillTime(drillItem);
//            Log.info("Drill progress: @, delay: @, dominantItems: @, itemCapacity: @, items.total(): @", progress, delay, dominantItems, itemCapacity, items.total());

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
                    if(!consumeOre(drillItem)) break;
                    offload(drillItem);
                }

                progress %= delay;

                if(wasVisible && Mathf.chanceDelta(drillEffectChance * warmup)) drillEffect.at(x + Mathf.range(drillEffectRnd), y + Mathf.range(drillEffectRnd), drillItem.color);
            }
        }

        private boolean consumeOre(Item item){
            for(int dx = 0; dx < size; dx++){
                for(int dy = 0; dy < size; dy++){
                    Tile oreTile = Vars.world.tile(tile.x + dx, tile.y + dy);
                    if(oreTile == null || !(oreTile.overlay() instanceof LimitedOre frostOre) || frostOre.itemDrop != item) continue;                    
                    int remaining = frostOre.getOreCount(oreTile);
                    if(remaining <= 0) continue;

                    frostOre.setOreCount(oreTile, remaining - 1);
                    return true;
                }
            }
            return false;
        }
    }
}