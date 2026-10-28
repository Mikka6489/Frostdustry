package frostdustry.world.blocks.heat;

import java.util.*;
import arc.math.*;
import arc.struct.*;
import mindustry.world.blocks.heat.*;
import mindustry.world.blocks.production.*;

import static mindustry.Vars.*;

public class HeatPipeold extends HeatConductor {
    public HeatPipeold(String name){
        super(name);
        splitHeat = true;
	    rotateDraw = true;
    }
    
    public class HeatPipeBuild extends HeatConductorBuild{
    
        @Override	
        public float calculateHeat(float[] sideHeat, IntSet cameFrom) {
            Arrays.fill(sideHeat, 0.0F);
            if (cameFrom != null) cameFrom.clear();
                float heat = 0.0F;
                for (var build : proximity) {
                    if (build != null && build.team == team && build instanceof HeatBlock heater) {
                        boolean split = build.block instanceof HeatConductor cond && cond.splitHeat;
                        if (!build.block.rotate || (!split && (relativeTo(build) + 2) % 4 == build.rotation) || (split && relativeTo(build) != build.rotation)) {
                            if (!(build instanceof HeatConductorBuild hc && hc.cameFrom.contains(id()))) {
                                float diff = (Math.min(Math.abs(build.x - x), Math.abs(build.y - y)) / tilesize);
                                int contactPoints = Math.min((int)(block.size / 2.0F + build.block.size / 2.0F - diff), Math.min(build.block.size, block.size));
                                float add = heater.heat() / build.block.size * contactPoints;
                                if (split) {
                                    add /= 1.01F;
                                }
                                sideHeat[Mathf.mod(relativeTo(build), 4)] += add;
                                heat += add;
                            }
                            if (cameFrom != null) {
                                cameFrom.add(build.id);
                                if (build instanceof HeatConductorBuild hc) {
                                    cameFrom.addAll(hc.cameFrom);
                                }
                            }
                            if (heater instanceof HeatConductorBuild cond) {
                                cond.updateHeat();
                            }
                        }
                    }
                }
                return heat;
            }
        }
    }

