package frostdustry.world.blocks.liquid;

import arc.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.*;
import mindustry.world.meta.*;

import static mindustry.Vars.*;

public class LiquidSorter extends Block{
    //public @Load(value = "@-cross", fallback = "cross-full") TextureRegion cross;
    public boolean invert;

    public LiquidSorter(String name){
        super(name);
        update = false;
        destructible = true;
        underBullets = true;
        instantTransfer = true;
        group = BlockGroup.liquids;
        hasLiquids = true;
        outputsLiquid = true;
        liquidCapacity = 10f;
        configurable = true;
        unloadable = false;
        saveConfig = true;
        clearOnDoubleTap = true;

        config(Liquid.class, (LiquidSorterBuild tile, Liquid liquid) -> tile.sortLiquid = liquid);
        configClear((LiquidSorterBuild tile) -> tile.sortLiquid = null);
    }

    @Override
    public void drawPlanConfig(BuildPlan plan, Eachable<BuildPlan> list){
        drawPlanConfigCenter(plan, plan.config, "center", true);
    }

    @Override
    public int minimapColor(Tile tile){
        var build = (LiquidSorterBuild)tile.build;
        return build == null || build.sortLiquid == null ? 0 : build.sortLiquid.color.rgba();
    }

    @Override
    protected TextureRegion[] icons(){
        return new TextureRegion[]{Core.atlas.find("source-bottom"), region};
    }

    public class LiquidSorterBuild extends Building{
        public @Nullable Liquid sortLiquid;

        @Override
        public void configured(Unit player, Object value){
            super.configured(player, value);

            if(!headless){
                renderer.minimap.update(tile);
            }
        }

        @Override
        public void draw(){

            if(sortLiquid == null){
                //Draw.rect(cross, x, y);
            }else{
                Draw.color(sortLiquid.color);
                Fill.square(x, y, tilesize/2f - 0.00001f);
                Draw.color();
            }

            super.draw();
        }

        @Override
        public void drawSelect(){
            super.drawSelect();
            drawItemSelection(sortLiquid);
        }

        @Override
        public boolean acceptLiquid(Building source, Liquid liquid){
            Building to = getTileTarget(liquid, source, false);

            return to != null && to.acceptLiquid(this, liquid) && to.team == team;
        }

        @Override
        public void handleLiquid(Building source, Liquid liquid, float amount){
            Building to = getTileTarget(liquid, source, true);
            if(to != null){
                to.handleLiquid(this, liquid, amount);
            }
        }

        public boolean isSame(Building other){
            return other != null && other.block.instantTransfer;
        }

        public Building getTileTarget(Liquid liquid, Building source, boolean flip){
            int dir = source.relativeTo(tile.x, tile.y);
            if(dir == -1) return null;
            Building to;

            if(((liquid == sortLiquid) != invert) == enabled){
                //prevent 3-chains
                if(isSame(source) && isSame(nearby(dir))){
                    return null;
                }
                to = nearby(dir);
            }else{
                Building a = nearby(Mathf.mod(dir - 1, 4));
                Building b = nearby(Mathf.mod(dir + 1, 4));
                boolean ac = a != null && !(a.block.instantTransfer && source.block.instantTransfer) &&
                a.acceptLiquid(this, liquid);
                boolean bc = b != null && !(b.block.instantTransfer && source.block.instantTransfer) &&
                b.acceptLiquid(this, liquid);

                if(ac && !bc){
                    to = a;
                }else if(bc && !ac){
                    to = b;
                }else if(!bc){
                    return null;
                }else{
                    to = (rotation & (1 << dir)) == 0 ? a : b;
                    if(flip) rotation ^= (1 << dir);
                }
            }

            return to;
        }

        @Override
        public void buildConfiguration(Table table){
            ItemSelection.buildTable(LiquidSorter.this, table, content.liquids(), () -> sortLiquid, this::configure, selectionRows, selectionColumns);
        }

        @Override
        public Liquid config(){
            return sortLiquid;
        }

        @Override
        public byte version(){
            return 3;
        }

        @Override
        public void write(Writes write){
            super.write(write);
            write.s(sortLiquid == null ? -1 : sortLiquid.id);
        }

        @Override
        public void read(Reads read, byte revision){
            super.read(read, revision);
            if(revision <= 2){
                read.s();
                sortLiquid = null;
            }else{
                sortLiquid = content.liquid(read.s());
            }

            if(revision == 1){
                new DirectionalItemBuffer(20).read(read);
            }
        }
    }
}
