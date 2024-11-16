package frostdustry.content;

import arc.graphics.*;
import mindustry.content.*;
import mindustry.type.*;
import mindustry.world.*;
import frostdustry.world.blocks.defense.*;
import frostdustry.world.blocks.production.*;
import frostdustry.content.*;
import frostdustry.world.*;
import mindustry.world.blocks.storage.*;
import mindustry.world.*;
import mindustry.world.blocks.*;

import mindustry.type.weather.*;

import static mindustry.type.ItemStack.*;

public class FrostBlocks{
    
    public static Block reverseOverdrive, generator, graphiteClamp, refridgerator;

    public static void load() {
            reverseOverdrive = new ReverseOverdrive("reverse-overdrive"){{
            requirements(Category.effect, with(Items.copper, 60, Items.sand, 15, Items.metaglass, 40));
                size = 2;
                health = 300;
//                powerProduction = 8.5f;
//                ambientSound = Sounds.pulse;
//                ambientSoundVolume = 0.05f;
//                consumePower(2.5f);
//                consumeLiquid(Liquids.water, 0.1f);
//                outputItem = new ItemStack(Items.graphite, 1);
                reload = 1f;
                range = 9999f;
                speedBoost = 1.5f;
                speedBoostPhase = 0.75f;
                useTime = 400f;
                phaseRangeBoost = 20f;
                hasBoost = true;
                baseColor = Color.valueOf("feb380");
                phaseColor = Color.valueOf("ffd59e");
            }};

            generator = new CoreBlock("generator"){{
                requirements(Category.effect, with(Items.copper, 60, Items.sand, 15, Items.metaglass, 40));
                size = 5;
                health = 5000;
                itemCapacity = 9000;
                consumeLiquid(Liquids.oil, 0.1f);
            }};

            graphiteClamp = new FrostGCrafter("graphite-clamp"){{
                requirements(Category.production, with(Items.copper, 60, Items.sand, 15, Items.metaglass, 40));
                size = 2;
                health = 100;
                consumeLiquid(Liquids.water, 0.1f);
                outputItem = new ItemStack(Items.graphite, 1);
            }};
/*
            refridgerator = new FrostBlock("refridgerator"){{
                requirements(Category.effect, with(Items.copper, 60, Items.sand, 15, Items.metaglass, 40));
                size = 2;
                health = 100;
                attrs.set(FrostAttribute.cold, 1f);
            }};
*/
        };
    }
