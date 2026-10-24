package frostdustry.world.blocks.heat;

import arc.util.*;
import mindustry.world.blocks.heat.*;

public class HeatPipe extends HeatConductor {
    public HeatPipe(String name){
        super(name);
        splitHeat = true;
    }
    
    public class HeatPipeBuild extends HeatConductorBuild {
        @Override
        public void updateTile(){
            super.updateTile();
            Log.info(heatFrac());
        }
        @Override
        public float heatFrac(){
            Log.info("TEST NIGGA");
            return (heat / visualMaxHeat);
        }
    }
}
