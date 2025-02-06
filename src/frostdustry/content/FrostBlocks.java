package frostdustry.content;

import mindustry.content.*;
import mindustry.type.*;
import mindustry.world.*;
import frostdustry.world.*;
import frostdustry.world.blocks.defense.*;
import frostdustry.world.blocks.production.*;

import static mindustry.type.ItemStack.*;

public class FrostBlocks{
    
    public static Block generator, graphiteClamp, refridgerator, heater, ohno, graphiteClamp2;

    public static void load() {
                generator = new Heater("generator"){{
                requirements(Category.effect, with(Items.copper, 60, Items.sand, 15, Items.metaglass, 40));
                size = 5;
                health = 5000;
                itemCapacity = 9000;
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

            graphiteClamp2 = new FrostACrafter("graphite-clamp2"){{
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

            ohno = new FrostBlock("ohno"){{
                requirements(Category.effect, with(Items.lead, 100));
                size = 2;
            }};

            heater = new Heater("heater"){{
                requirements(Category.effect, with(Items.lead, 100, Items.titanium, 75, Items.silicon, 75, Items.plastanium, 30));
//                consumePower(3.50f);
                size = 2;
            }};
        };
    }
