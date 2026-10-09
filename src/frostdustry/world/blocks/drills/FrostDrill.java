package frostdustry.world.blocks.drills;

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
        if (super.getDrillTime(item) * (FrostMethods.calcCold(recievedHeat) * 2f + 1f) > 2500f) {
            return 99999;
        } else{
            return super.getDrillTime(item) * (FrostMethods.calcCold(recievedHeat) * 2f + 1f);
        }
//        return super.getDrillTime(item) * (FrostMethods.calcCold(recievedHeat) * 2f + 1f);
    }
}