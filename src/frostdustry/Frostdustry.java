package frostdustry;

import frostdustry.logic.*;
import frostdustry.content.*;
import mindustry.mod.*;
import frostdustry.world.*;
//import mindustry.mod.Mods.*;

//import arc.util.*;

public class Frostdustry extends Mod{
/*	@Override
	public void init(){
		TemperatureHandler.init();
	}
    
	public Frostdustry() {
		Log.info("yeahhhhh" + TemperatureHandler.test);
	}
*/

	@Override
    public void loadContent(){
        FrostBlocks.load();
        FrostWeather.load();
	TemperatureHandler.load();
    }
}
