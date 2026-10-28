package frostdustry;

import arc.util.*;
import mindustry.*;
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

    @Override
    public void registerClientCommands(CommandHandler handler){
        handler.register("cold", "<value>", "Set the current cold attribute.", args -> {
            float value = Float.parseFloat(args[0]);
            Vars.state.rules.attributes.set(FrostMethods.cold, value);
            Vars.state.envAttrs.set(FrostMethods.cold, value);
            Log.info("Cold set to @", value);
        });
    }
}
