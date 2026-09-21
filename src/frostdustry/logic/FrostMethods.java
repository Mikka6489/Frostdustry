package frostdustry.logic;

import mindustry.world.meta.*;

public class FrostMethods {
    public float calcCold;
    public float recievedHeat;
    public Attribute cold = FrostVars.cold;

    public float calcCold(float recievedHeat){
        calcCold = cold.env() - recievedHeat;
        if (calcCold < 0f) calcCold = 0f;
        return calcCold;
    }
}
