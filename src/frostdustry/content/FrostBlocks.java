package frostdustry.content;

import arc.graphics.*;
import mindustry.content.*;
import mindustry.type.*;
import mindustry.world.*;
import frostdustry.world.blocks.defense.*;
import frostdustry.world.blocks.production.*;
import mindustry.world.blocks.storage.*;

import static mindustry.type.ItemStack.*;

public class FrostBlocks{
    
    public static Block reverseOverdrive, generator, graphiteClamp, refridgerator, heater;

    public static void load() {
            reverseOverdrive = new ReverseOverdrive("reverse-overdrive"){{
            requirements(Category.effect, with(Items.copper, 60, Items.sand, 15, Items.metaglass, 40));
                size = 2;
                health = 300;
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
                requirements(Category.crafting, with(Items.copper, 75, Items.lead, 30));
                craftEffect = Fx.pulverizeMedium;
                outputItem = new ItemStack(Items.graphite, 1);
                craftTime = 90f;
                size = 2;
                hasItems = true;
                consumeItem(Items.coal, 2);
            }};
/*
            refridgerator = new FrostBlock("refridgerator"){{
                requirements(Category.effect, with(Items.copper, 60, Items.sand, 15, Items.metaglass, 40));
                size = 2;
                health = 100;
                attrs.set(FrostAttribute.cold, 1f);
            }};
*/
            heater = new Heater("heater"){{
                requirements(Category.effect, with(Items.lead, 100, Items.titanium, 75, Items.silicon, 75, Items.plastanium, 30));
//                consumePower(3.50f);
                size = 2;
//                consumeItem(Items.phaseFabric).boost();
            }};
        };
    }
