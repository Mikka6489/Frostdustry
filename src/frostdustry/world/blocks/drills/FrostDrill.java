package frostdustry.world.blocks.drills;

import java.util.*;
import arc.*;
import arc.util.*;
import arc.math.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.graphics.*;
import mindustry.ui.*;
import mindustry.type.*;
import mindustry.world.blocks.environment.*;
import mindustry.world.blocks.production.*;
import mindustry.world.*;
import mindustry.world.meta.*;

import frostdustry.logic.*;
import frostdustry.type.*;
import frostdustry.world.blocks.environment.*;

import static mindustry.Vars.*;

public class FrostDrill extends Drill {  
	public boolean limitedDrill = false;
	List<Block> allowedMineables = new ArrayList<>();
    public FrostDrill(String name) {
        super(name);
    }
	    public void setMineables(){
		for (Block block : Vars.content.blocks()){
			if (block instanceof LimitedOre ore && ore.itemDrop.hardness <= tier){
				allowedMineables.add(block);
			}
		}
		allowedMineables.add(Blocks.darksand);
    	}


	@Override
	public void init(){
		super.init();
		setMineables();
	}

    @Override
    public boolean canMine(Tile tile){
        return limitedDrill ? tile != null && allowedMineables.contains(tile.overlay()) || (allowedMineables.contains(tile.floor()) && tile.overlay() == Blocks.air) && super.canMine(tile) : tile != null && !(tile.overlay() instanceof LimitedOre) && super.canMine(tile);
    }

    @Override
    public void setStats(){
        super.setStats();
        if (limitedDrill) {
            stats.replace(Stat.drillTier, StatValues.drillables(drillTime, hardnessDrillMultiplier, size * size, drillMultipliers, block ->
                block instanceof Floor ore && allowedMineables.contains(ore) && !ore.wallOre && ore.itemDrop != null &&
                ore.itemDrop.hardness <= tier && (blockedItems == null || !blockedItems.contains(ore.itemDrop)) &&
                (indexer.isBlockPresent(ore) || state.isMenu())
            ));
        }else{
            stats.replace(Stat.drillTier, StatValues.drillables(drillTime, hardnessDrillMultiplier, size * size, drillMultipliers, block ->
                block instanceof Floor floor && !(floor instanceof LimitedOre) && !floor.wallOre && floor.itemDrop != null &&
                floor.itemDrop.hardness <= tier && (blockedItems == null || !blockedItems.contains(floor.itemDrop)) &&
                (indexer.isBlockPresent(floor) || state.isMenu())
            ));
        }
    }

    @Override
    public void setBars(){
        super.setBars();
	    removeBar("drillspeed");
        addBar("craftspeed", (FrostDrillBuild e) -> {
            return new Bar(
                () -> Core.bundle.format(
                    "bar.craftspeed", 
                    Strings.fixed(e.nonColdDrillSpeed * 60f * e.timeScale(), 2),
                    Strings.fixed(e.lastDrillSpeed * 60f * e.timeScale(), 2)
                ),
                () -> Pal.ammo,
                () -> e.warmup
            );
        });

        if (limitedDrill){
            addBar("orecount", entity -> {
                FrostDrillBuild build = (FrostDrillBuild) entity;
                return new Bar(
                    () -> "Ore: " + build.totalOreCount(),
                    () -> Pal.techBlue,
                    () -> Mathf.clamp(build.totalOreCount() / (float)(size * size * 20), 0f, 1f)
                );
            });
        }
    }

    public class FrostDrillBuild extends DrillBuild implements HeatReciever{
        public float recievedHeat, heatDuration, nonColdDrillSpeed;
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
        public void draw(){
            super.draw();
            if (FrostMethods.calcCold(recievedHeat) > 3f) {//baseReloadSpeed() < -1f
                FrostMethods.drawStoppedOverlay(this);
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
		//Log.info(lastDrillSpeed);
            float delay = getDrillTime(dominantItem) * (FrostMethods.calcCold(recievedHeat) * 2f + 1f);
	        float nonColdDelay = getDrillTime(dominantItem);

	        if (FrostMethods.calcCold(recievedHeat) > 3f) efficiency = 0; //delay = 2500f

            if(items.total() < itemCapacity && dominantItems > 0 && efficiency > 0){
                float speed = Mathf.lerp(1f, liquidBoostIntensity, optionalEfficiency) * efficiency;

                lastDrillSpeed = (speed * dominantItems * warmup) / delay;
		        nonColdDrillSpeed = (speed * dominantItems * warmup) / nonColdDelay;
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
		            if (allowedMineables.contains(tile.overlay()) && dominantItem != null && limitedDrill){
                        if(!consumeOre(dominantItem)) break;
			        }
			        if (dominantItem != null){
                		offload(dominantItem);
			
                    }
                }

                progress %= delay;

                if(wasVisible && Mathf.chanceDelta(drillEffectChance * warmup)) drillEffect.at(x + Mathf.range(drillEffectRnd), y + Mathf.range(drillEffectRnd), dominantItem.color);
            }
        }

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
