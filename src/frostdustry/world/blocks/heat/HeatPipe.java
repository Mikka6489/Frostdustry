package frostdustry.world.blocks.heat;

import java.util.*;
import arc.math.*;
import arc.struct.*;
import mindustry.world.blocks.heat.*;
import mindustry.world.blocks.production.*;

import static mindustry.Vars.*;

public class HeatPipe extends HeatConductor {
    public HeatPipe(String name){
        super(name);
        splitHeat = true;
	    rotateDraw = true;
    }
    
    public class HeatPipeBuild extends HeatConductorBuild{
    
        @Override	
        public float calculateHeat(float[] sideHeat, IntSet cameFrom) {
            Arrays.fill(sideHeat, 0.0F);
            if (cameFrom != null) cameFrom.clear();
            Seq<HeatPipeBuild> network = new Seq<>();
            IntSet networkIds = new IntSet();
            network.add(this);
            networkIds.add(id());

            for (int i = 0; i < network.size; i++) {
                HeatPipeBuild pipe = network.get(i);
                for (var build : pipe.proximity) {
                    if (build != null && build.team == team && build instanceof HeatPipeBuild other && !networkIds.contains(other.id())) {
                        networkIds.add(other.id());
                        network.add(other);
                    }
                }
            }

            if (cameFrom != null) {
                for (HeatPipeBuild pipe : network) {
                    cameFrom.add(pipe.id());
                }
            }

            float heat = 0f;
            for (HeatPipeBuild pipe : network) {
                for (var build : pipe.proximity) {
                    if (build == null || build.team != team || networkIds.contains(build.id()) || !(build instanceof HeatBlock heater)) continue;

                    boolean split = build.block instanceof HeatConductor cond && cond.splitHeat;
                    int direction = pipe.relativeTo(build);
                    if (build.block.rotate && (split ? direction == build.rotation : (direction + 2) % 4 != build.rotation)) continue;
                    if (build instanceof HeatConductorBuild hc && intersectsNetwork(hc.cameFrom, network)) continue;

                    float diff = Math.min(Math.abs(build.x - pipe.x), Math.abs(build.y - pipe.y)) / tilesize;
                    int contactPoints = Math.min((int)(pipe.block.size / 2.0F + build.block.size / 2.0F - diff), Math.min(build.block.size, pipe.block.size));
                    if (contactPoints <= 0) continue;

                    float add = heater.heat() / build.block.size * contactPoints;
                    if (split) add /= 1.01F;

                    if (pipe == this) {
                        sideHeat[Mathf.mod(relativeTo(build), 4)] += add;
                    }
                    heat += add;

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

            Map<Integer, float[]> transfers = new HashMap<>();
            for (HeatPipeBuild pipe : network) {
                for (var build : pipe.proximity) {
                    if (build == null || build.team != team || !(build.block instanceof HeatCrafter) || !(build instanceof HeatConsumer receiver)) continue;
                    boolean split = pipe.block instanceof HeatConductor cond && cond.splitHeat;
                    int direction = pipe.relativeTo(build);
                    if ((!split && direction != pipe.rotation) || (split && direction == (pipe.rotation + 2) % 4)) continue;

                    float diff = Math.min(Math.abs(build.x - pipe.x), Math.abs(build.y - pipe.y)) / tilesize;
                    int contactPoints = Math.min((int)(pipe.block.size / 2.0F + build.block.size / 2.0F - diff), Math.min(build.block.size, pipe.block.size));
                    float requirement = receiver.heatRequirement();
                    if (contactPoints <= 0 || requirement <= 0f) continue;

                    float factor = (float)contactPoints / pipe.block.size / (split ? 3f : 1f);
                    float[] transfer = transfers.get(build.id());
                    if (transfer == null) {
                        transfers.put(build.id(), new float[]{factor, requirement});
                    } else {
                        transfer[0] += factor;
                    }
                }
            }

            if (!transfers.isEmpty() && heat > 0f) {
                float low = 0f, high = heat;
                for (int i = 0; i < 32; i++) {
                    float remaining = (low + high) / 2f;
                    float sent = 0f;
                    for (float[] transfer : transfers.values()) {
                        sent += Math.min(remaining * transfer[0], transfer[1]);
                    }
                    if (remaining + sent > heat) {
                        high = remaining;
                    } else {
                        low = remaining;
                    }
                }

                float remainingHeat = (low + high) / 2f;
                float scale = remainingHeat / heat;
                for (int i = 0; i < sideHeat.length; i++) {
                    sideHeat[i] *= scale;
                }
                heat = remainingHeat;
            }

            return heat;
        }

        private boolean intersectsNetwork(IntSet ids, Seq<HeatPipeBuild> network) {
            for (HeatPipeBuild pipe : network) {
                if (ids.contains(pipe.id())) return true;
            }
            return false;
        }
    }
}
