package frostdustry.world.blocks.defense;

import arc.util.*;
import arc.util.io.Reads;
import arc.util.io.Writes;
import arc.math.*;
import arc.struct.*;
import mindustry.world.*;
import mindustry.gen.*;
import mindustry.logic.*;
import mindustry.game.*;
import mindustry.*;
import mindustry.ctype.ContentType;
import mindustry.type.*;
import mindustry.world.consumers.*;
import mindustry.world.meta.*;

import frostdustry.logic.*;
import frostdustry.type.*;

import static mindustry.Vars.*;

public class BlastFurnace extends Block {
    public float warmupSpeed = 1f;
    public float range = 800f;
    public float updateTime = 60f;
    
    public BlastFurnace(String name){
        super(name);
        solid = true;
        update = true;
        group = BlockGroup.projectors;
        hasPower = false;
        emitLight = true;
        lightRadius = 50f;
    }

    @Override
    public void init(){
        consume(new ConsumeLiquidFilter(liquid -> liquid instanceof FrostLiquid heatingLiquid && heatingLiquid.heatUpgrade > 1, 0.235f));
        super.init();
    }

    @Override
    public boolean canPlaceOn(Tile tile, Team team, int rotation){
        Seq<Tile> linkedTiles = tile.getLinkedTilesAs(this, new Seq<>());
        if(linkedTiles.size == 0) return false;

        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE;
        for(Tile linked : linkedTiles){
            minX = Math.min(minX, linked.x);
            minY = Math.min(minY, linked.y);
            maxX = Math.max(maxX, linked.x);
            maxY = Math.max(maxY, linked.y);
        }

        for(int x = minX; x <= maxX; x++){
            if(isValidNeighbour(x, minY - 1) || isValidNeighbour(x, maxY + 1)) return true;
        }
        for(int y = minY; y <= maxY; y++){
            if(isValidNeighbour(minX - 1, y) || isValidNeighbour(maxX + 1, y)) return true;
        }
        return false;
    }

    private boolean isValidNeighbour(int x, int y){
        Tile tile = Vars.world.tile(x, y);
        return tile != null && tile.build != null
            && (tile.build.block instanceof Generator || tile.build.block instanceof BlastFurnace);
    }

    public class BlastFurnaceBuild extends Building implements Ranged{
        public float progress, totalProgress, warmup, heatUpgradeDuration, heat, charge;
        public int personalHeatLevel;
        public boolean contributing;

        @Override
        public void update(){
            super.update();
            FrostLiquid heatingLiquid = getHeatingLiquid();
            updateContribution(heatingLiquid == content.getByName(ContentType.liquid, "frostdustry-antifreeze") && efficiency > 0f);
            if (heatingLiquid != null) {
                FrostMethods.maxHeatUpgrade = heatingLiquid.heatUpgrade < 5 ? heatingLiquid.heatUpgrade : 2 * FrostMethods.runningUpgraders;
            }
        }

        @Override
        public void updateTile(){

            //progress += getProgressIncrease(updateTime);
            warmup = Mathf.approachDelta(warmup, 1f, warmupSpeed);
            heat = Mathf.lerpDelta(heat, efficiency > 0 ? 1f : 0f, 0.08f);
            charge += heat * Time.delta;
            //plasmaProgress += heat * Time.delta;

            if(charge >= updateTime){
                charge = 0f;
                FrostLiquid heatingLiquid = getHeatingLiquid();
                if(heatingLiquid != null){
                    indexer.eachBlock(this, range,
                        other -> other instanceof Generator.GeneratorBuild generatorBuild && generatorBuild.block instanceof Generator generator && generator.canBurnCoal,
                        other -> ((Generator.GeneratorBuild)other).updateHeatLevel(FrostMethods.maxHeatUpgrade, 80f));
                }
            }

            /*if(progress >= 1){
                progress %= 1;
            }*/
        }

        private void updateContribution(boolean canConsumeFuel){
            if(canConsumeFuel == contributing) return;

            contributing = canConsumeFuel;
            FrostMethods.runningUpgraders = Math.max(0, FrostMethods.runningUpgraders + (contributing ? 1 : -1));
        }

        private FrostLiquid getHeatingLiquid(){
            Liquid consumedLiquid = liquids.current();
            return consumedLiquid instanceof FrostLiquid heatingLiquid ? heatingLiquid : null;
        }

        @Override
        public void onRemoved(){
            updateContribution(false);
            super.onRemoved();
        }

        @Override
        public float range(){
            return range;
        }

        @Override
        public void write(Writes write){
            super.write(write);
            write.f(heat);
            write.i(FrostMethods.runningUpgraders);
        }

        @Override
        public void read(Reads read, byte revision){
            super.read(read, revision);
            heat = read.f();
            read.i();
            contributing = false;
        }
    }
}