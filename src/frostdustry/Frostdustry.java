package frostdustry;

import frostdustry.logic.*;
import frostdustry.content.*;
import mindustry.mod.*;

public class Frostdustry extends Mod{
	@Override
    public void loadContent(){
        FrostPlanet.load();
        FrostWeather.load();
	    LogicHandler.load();
    }

    @Override
    public void init(){
        FrostTechTree.load();
    }
}
