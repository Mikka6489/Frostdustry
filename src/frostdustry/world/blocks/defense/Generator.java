package frostdustry.world.blocks.defense;

import arc.*;
import arc.graphics.*;
import arc.math.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.graphics.*;
import mindustry.content.*;
import mindustry.gen.*;
import mindustry.logic.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.world.meta.*;
import mindustry.world.*;
import mindustry.world.draw.*;
import frostdustry.logic.*;
import frostdustry.type.*;

import static mindustry.Vars.*;

public class Generator extends Block{
    @Deprecated
	public boolean generatorActive = FrostMethods.generatorActive;
	public int runningHeaters = FrostMethods.runningHeaters;
    public boolean canBurnCoal = true;    
    public DrawBlock drawer = new DrawDefault();
    public int heatLevel = 1;

    public float heat = 1.5f;
    public float reload = 60f;
    public float range = 80f;
    public float useTime = 400f;
    public Color baseColor = Color.valueOf("feb380");
//    float heaterBonus = FrostAttribute.globalHeaters * 0.1f;

    public Generator(String name){
        super(name);
        solid = true;
        update = true;
        group = BlockGroup.projectors;
        hasPower = true;
        hasItems = true;
        itemCapacity = 30;
        emitLight = true;
        lightRadius = 50f;
        drawer = new DrawMulti(new DrawRegion("-bottom"), new DrawPlasma(), new DrawDefault());
        envEnabled |= Env.space;
    }

    @Override
    public void load(){
        super.load();
        drawer.load(this);
    }

    public int coalCost(){
        return Math.max(1, (int)(5f * runningHeaters * heatLevel));
    }

    private HeatReciever heatReciever(Building building){
        if(building instanceof HeatReciever receiver) return receiver;
        if(building.block instanceof HeatReciever receiver) return receiver;
        return null;
    }
/*
    @Override
    public boolean outputsItems(){
        return false;
    }
*/
    @Override
    public void drawPlace(int x, int y, int rotation, boolean valid){
        super.drawPlace(x, y, rotation, valid);

        Drawf.dashCircle(x * tilesize + offset, y * tilesize + offset, range, baseColor);

        indexer.eachBlock(player.team(), x * tilesize + offset, y * tilesize + offset, range, other -> {
            HeatReciever receiver = heatReciever(other);
            return receiver != null && receiver.canBeHeated();
        }, other -> Drawf.selected(other, Tmp.c1.set(baseColor).a(Mathf.absin(4f, 1f))));
    }

    @Override
    public void setStats(){
        stats.timePeriod = useTime;
        super.setStats();

//        stats.add(Stat.speedIncrease, "+" + (int)(speedBoost * 100f - 100) + "%");
        stats.add(Stat.range, range / tilesize, StatUnit.blocks);
        stats.add(Stat.productionTime, useTime / 60f, StatUnit.seconds);
  }
    
    @Override
    public void setBars(){
        super.setBars();
//        addBar("boost", (GeneratorBuild entity) -> new Bar(() -> Core.bundle.format("bar.boost", Mathf.round(Math.max((entity.realBoost() * 100 - 100), 0))), () -> Pal.accent, () -> entity.realBoost() / speedBoost));
    }

    public class GeneratorBuild extends Building implements Ranged{
        public float heat, charge = Mathf.random(reload), phaseHeat, smoothEfficiency, useProgress, plasmaProgress;
        public boolean nowFueled;

        public void updateHeaterStatus(){
            if (canBurnCoal){
                    int coalCost = coalCost();
                    if(items.get(Items.coal) >= coalCost){
                        items.remove(Items.coal, coalCost);
                        generatorActive = true;
                    } else { generatorActive = false; }
                }
        }

        @Override
        public void created(){
            super.created();
            runningHeaters++;
            updateHeaterStatus();
        }

        @Override
        public void onRemoved(){
            runningHeaters--;
            super.onRemoved();
        }

        @Override
        public boolean acceptItem(Building source, Item item){
            return item == Items.coal && items.get(item) < itemCapacity;
        }

        @Override
        public float range(){
            return range;
        }

        @Override
        public void draw(){
            drawer.draw(this);
        }

        @Override
        public void drawLight(){
            Drawf.light(x, y, lightRadius * smoothEfficiency, baseColor, 0.7f * smoothEfficiency);
        }

        @Override
        public void updateTile(){
            if (generatorActive) {
                smoothEfficiency = Mathf.lerpDelta(smoothEfficiency, efficiency, 0.08f);
                heat = Mathf.lerpDelta(heat, efficiency > 0 ? 1f : 0f, 0.08f);
                charge += heat * Time.delta;
            } else {
                smoothEfficiency = 0f;
                heat = 0f;
                //charge = 0f;
            }

            plasmaProgress += heat * Time.delta;

            if(charge >= reload){
                float realRange = range + phaseHeat;
                charge = 0f;
                indexer.eachBlock(this, realRange, other -> {
                    HeatReciever receiver = heatReciever(other);
                    return receiver != null && receiver.canBeHeated();
                }, other -> heatReciever(other).recieveHeat(heat));
            }
            if(efficiency > 0){
                useProgress += delta();
            }

            if(useProgress >= useTime){
                updateHeaterStatus();
                useProgress %= useTime;
            }
        }

/*
        public float realBoost(){
            return (speedBoost + phaseHeat * speedBoostPhase) * efficiency;
        }
*/
        @Override
        public float warmup(){
            return heat;
        }

        @Override
        public float totalProgress(){
            return plasmaProgress;
        }

        @Override
        public void drawSelect(){
            float realRange = range + phaseHeat;

            indexer.eachBlock(this, realRange, other -> {
                HeatReciever receiver = heatReciever(other);
                return receiver != null && receiver.canBeHeated();
            }, other -> Drawf.selected(other, Tmp.c1.set(baseColor).a(Mathf.absin(4f, 1f))));

            Drawf.dashCircle(x, y, realRange, baseColor);
        }

        @Override
        public void write(Writes write){
            super.write(write);
            write.f(heat);
            write.f(phaseHeat);
        }

        @Override
        public void read(Reads read, byte revision){
            super.read(read, revision);
            heat = read.f();
            phaseHeat = read.f();
        }
    }
}
