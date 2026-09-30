package frostdustry.logic;

import arc.*;
import arc.util.*;
import mindustry.game.EventType.*;
import mindustry.*;
import mindustry.world.Tile;
import mindustry.world.blocks.*;

import frostdustry.world.blocks.environment.*;

public class TemperatureHandler{
	public static Attributes attrs = new Attributes();

	public static void load() {
		Events.on(WorldLoadEvent.class, e -> {
			for(Tile tile : Vars.world.tiles){
				if(tile.overlay() instanceof FrostOre ore){
					if (ore.getOreCount(tile) != 0) continue;
					ore.seedRandom(tile);
				}
			}
		});

		Events.on(WaveEvent.class, e -> {
			int wave = Vars.state.wave;
			if (wave % 1 == 0) {
				Log.info("cold: " + FrostVars.cold.env());
				Vars.state.rules.attributes.set(FrostVars.cold, FrostVars.cold.env() + 0.1f);
				Vars.state.envAttrs.set(FrostVars.cold, FrostVars.cold.env() + 0.1f);
			}
		});

	};
}

