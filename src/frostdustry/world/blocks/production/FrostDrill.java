package frostdustry.world.blocks.production;

import mindustry.type.Item;
import mindustry.world.blocks.production.*;
import frostdustry.logic.FrostMethods;
import frostdustry.type.*;

public class FrostDrill extends Drill implements HeatReciever{
    public float recievedHeat = 0.0001f;
    FrostMethods FrostMethods = new FrostMethods();

    public FrostDrill(String name) {
        super(name);
    }

    public void recieveHeat(float heat) {
        recievedHeat = heat;
    }

    public boolean canBeHeated() {
        return true;
    }

    @Override 
    public float getDrillTime(Item item){
        if ((drillTime + hardnessDrillMultiplier * item.hardness) / drillMultipliers.get(item, 1f) * (FrostMethods.calcCold(recievedHeat) * 2f + 1f) > 2500f) {
            return 99999;
        } else{
            return (drillTime + hardnessDrillMultiplier * item.hardness) / drillMultipliers.get(item, 1f) * (FrostMethods.calcCold(recievedHeat) * 2f + 1f);
        }
//        return (drillTime + hardnessDrillMultiplier * item.hardness) / drillMultipliers.get(item, 1f) * (calcCold() * 5f + 0.001f);
    }
}