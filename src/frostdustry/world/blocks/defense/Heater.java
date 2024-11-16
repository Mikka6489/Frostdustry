package frostdustry.world.blocks.defense;

import arc.graphics.*;
import arc.math.geom.*;
import mindustry.content.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.meta.*;
import arc.*;
import mindustry.entities.*;
import mindustry.graphics.*;
import mindustry.ui.*;
import arc.math.*;
import arc.util.*;
import mindustry.world.blocks.*;

import frostdustry.world.*;

public class Heater extends FrostBlock {

    public int radius = 5; // AoE radius in tiles
    public Item itemToGive = Items.copper; // Item to distribute
    public float distributionTime = 60f; // Time in ticks (1s = 60 ticks) between distributions

    public Heater(String name) {
        super(name);
        update = true; // This block needs continuous updates
        group = BlockGroup.transportation; // Categorize as a distribution block
        hasItems = true; // This block has an inventory
    }

    @Override
    public void setStats() {
        super.setStats();
        stats.add(Stat.range, radius * size, StatUnit.blocks);
    }

    @Override
    public void drawPlace(int x, int y, int rotation, boolean valid) {
        super.drawPlace(x, y, rotation, valid);
        // Draw a radius indicator when placing the block
        Drawf.dashCircle(x * size + offset, y * size + offset, radius * size, Color.yellow);
    }

    public class HeaterBuild extends FrostBuilding {

        private float timer = 0f;

        @Override
        public void updateTile() {
            timer += delta();

            if (timer >= distributionTime) {
                timer = 0f;
                distributeItems();
            }
        }

        private void distributeItems() {
            // Iterate through all buildings in the radius
            Units.nearby(team, x, y, radius * size, unit -> {
                if (unit instanceof FrostBuilding targetBuilding) {
                    // Check if the target can accept items
                    if (targetBuilding.acceptItem(this, itemToGive)) {
                        // Transfer an item to the target
                        handleItem(targetBuilding, itemToGive);
                    }
                }
            });
        }

        @Override
        public boolean acceptItem(Building source, Item item) {
            return items.total() < itemCapacity;
        }
    }
}
