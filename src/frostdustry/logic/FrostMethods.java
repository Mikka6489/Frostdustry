package frostdustry.logic;

import mindustry.world.meta.*;

public class FrostMethods {
    public static final Attribute
	cold = Attribute.add("cold");	

    public float calcCold;
    public float recievedHeat;
    public static boolean generatorActive;
    public static boolean fuelGenPlaced;
    public static int runningGenerators;
    public static int runningHeaters;

    public float calcCold(float recievedHeat){
        calcCold = cold.env() - recievedHeat;
        if (calcCold < 0f) calcCold = 0f;
        return calcCold;
    }
}
