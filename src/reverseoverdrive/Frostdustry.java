package frostdustry;

import frostdustry.content.*;
import mindustry.mod.*;

public class Frostdustry extends Mod{
    @Override
    public void loadContent(){
        StormBlock.load();
        StormWeather.load();
    }
}
