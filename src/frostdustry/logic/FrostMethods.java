package frostdustry.logic;

import arc.*;
import arc.util.*;
import arc.graphics.g2d.*;
import mindustry.gen.*;
import mindustry.world.meta.*;

public class FrostMethods {
    public static final Attribute
	cold = Attribute.add("cold");	

    public float calcCold;
    public static boolean generatorActive;
    public static boolean fuelGenPlaced;
    public static int runningGenerators;
    public static int globalHeatLevel;
    public static int globalCoalCost;
    public static int coalDropPerWave = 1;
	public void drawStoppedOverlay(Building build){
		int size = build.block.size;
		Draw.rect(Core.atlas.find("frostdustry-cold-" + size), build.x, build.y, size * 8f, size * 8f);
	}
    public float calcCold(float recievedHeat){
	return cold.env() - recievedHeat < 0f ? 0f : cold.env() - recievedHeat;
    }

    public void logUsefulShit(float recievedHeat) {
	    //Log.info("cold: @, recievedHeat: @, calcCold: @, calcCold < 0: @", cold.env(), recievedHeat, cold.env() - recievedHeat, cold.env() - recievedHeat < 0f);
    }
    public void logTurret(float reloadSpeed) {
	Log.info("reloadspeed: @", reloadSpeed);
    }
}
