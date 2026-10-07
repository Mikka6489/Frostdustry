package frostdustry.type;

import arc.graphics.Color;
import mindustry.type.*;

public class FrostLiquid extends Liquid{
    public int minHeatingRequired;
    public int maxHeatingRequired;
    public FrostLiquid(String name){
        super(name, new Color(Color.black));
    }
    
}
