package frostdustry.logic;

import arc.*;
import arc.util.*;
import mindustry.game.EventType.*;
import mindustry.*;
import mindustry.world.meta.*;
import mindustry.world.blocks.*;

import frostdustry.logic.*;

public class TemperatureHandler{

//	public float cold = FrostVars.cold;

	public static Attributes attrs = new Attributes();

	public static void load() {
		Events.on(WaveEvent.class, e -> {
			int wave = Vars.state.wave;
			if (wave % 1 == 0) {
				FrostVars.cold++;
				Log.info("varcold: " + FrostVars.cold + ", attrcold: " + FrostVars.coldattr.env());
				Vars.state.rules.attributes.set(FrostVars.coldattr, FrostVars.cold);
				Vars.state.envAttrs.set(FrostVars.coldattr, FrostVars.cold);
//				attrs.set(FrostVars.coldattr, FrostVars.cold);
				Log.info("wave change, cold: " + FrostVars.cold);
				//changeCold();
			}
		});
	};

	public void changeCold(){
		Log.info("uhhhh");
	};
}

