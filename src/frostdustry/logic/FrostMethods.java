package frostdustry.logic;

import arc.*;
import arc.graphics.g2d.*;
import mindustry.gen.*;
import mindustry.world.meta.*;

public class FrostMethods {
    public static final Attribute
	cold = Attribute.add("cold");	

    public static boolean generatorActive;
    public static int runningGenerators;
    public static int globalHeatLevel;
    public static int globalCoalCost;
    public static int coldDropPerWave = 10;

    public static int minHeatLevel = 0;
    public static int baseMaxHeat = 1;
    public static int maxHeatLevel = baseMaxHeat;
    public static int maxHeatUpgrade;
    public static int runningUpgraders;

	public void drawStoppedOverlay(Building build){
		int size = build.block.size;
		Draw.rect(Core.atlas.find("frostdustry-cold-" + size), build.x, build.y, size * 8f, size * 8f);
	}

    public float calcCold(float recievedHeat){
	    return cold.env() - recievedHeat < 0f ? 0f : cold.env() - recievedHeat;
    }
}
