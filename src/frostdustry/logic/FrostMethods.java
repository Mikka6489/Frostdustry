package frostdustry.logic;

import arc.util.*;
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
    public static int coalDropPerWave = 10;

    public float calcCold(float recievedHeat){
	return cold.env() - recievedHeat < 0f ? 0f : cold.env() - recievedHeat;
    }
}
