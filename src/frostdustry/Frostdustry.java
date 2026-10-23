package frostdustry;

import arc.util.*;
import mindustry.mod.*;

import frostdustry.logic.*;
import frostdustry.content.*;

public class Frostdustry extends Mod{
	@Override
    public void loadContent(){
        FrostPlanet.load();
        FrostWeather.load();
	    LogicHandler.load();
        UIHandler.load();
    }

    @Override
    public void init(){
        FrostTechTree.load();
    }
}
