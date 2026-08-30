package frostdustry.logic;

import arc.*;
import arc.util.*;
import mindustry.game.EventType.*;
import mindustry.*;
import mindustry.world.blocks.*;

public class TemperatureHandler{

//	public float cold = FrostVars.cold;

	public static Attributes attrs = new Attributes();

	public static void load() {
		Events.on(WaveEvent.class, e -> {
			int wave = Vars.state.wave;
			if (wave % 1 == 0) {
				Log.info("cold: " + FrostVars.cold.env());
				Vars.state.rules.attributes.set(FrostVars.cold, FrostVars.cold.env() + 1);
				Vars.state.envAttrs.set(FrostVars.cold, FrostVars.cold.env() + 1);
			}
		});
	};
}

