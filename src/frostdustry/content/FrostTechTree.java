package frostdustry.content;

//import mindustry.content.*;

import mindustry.ctype.ContentType;
import mindustry.world.Block;

import mindustry.Vars;

public class FrostTechTree {
    public static void load() {
        Block generator = Vars.content.getByName(ContentType.block, "frostdustry-generator");
        FrostPlanet.frostLand.techTree = generator.techNode;
    }
}
