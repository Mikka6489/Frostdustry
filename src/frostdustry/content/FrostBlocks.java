package frostdustry.content;

import mindustry.content.*;
import mindustry.type.*;
import mindustry.world.*;
import frostdustry.world.blocks.defense.*;
import frostdustry.world.blocks.production.*;

import static mindustry.type.ItemStack.*;

public class FrostBlocks{
    
    public static Block generator, frostdrill, graphiteClamp, refridgerator, heater, ohno, graphiteClamp2;

    public static void load() {
            generator = new Generator("generator"){{
                requirements(Category.effect, with(Items.copper, 60, Items.sand, 15, Items.metaglass, 40));
                size = 5;
                health = 5000;
                hasItems = true;
                itemCapacity = 9000;
            }};

            frostdrill = new FrostDrill("frostdrill"){{
                requirements(Category.effect, with(Items.copper, 60, Items.sand, 15, Items.metaglass, 40));
                size = 2;
                health = 5000;
                tier = 4;
                drillTime = 600;
//                hasItems = true;
//                itemCapacity = 9000;
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

            heater = new Heater("heater"){{
                requirements(Category.effect, with(Items.lead, 100, Items.titanium, 75, Items.silicon, 75, Items.plastanium, 30));
                size = 2;
                itemCapacity = 0;
            }};
        };
    }
