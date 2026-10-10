package frostdustry.logic;

import arc.*;
import arc.util.*;
import mindustry.game.EventType.*;
import mindustry.*;
import mindustry.world.*;
import mindustry.world.blocks.*;

import frostdustry.world.blocks.environment.*;

public class LogicHandler{
	public static Attributes attrs = new Attributes();

	public static void load() {
		Events.on(WorldLoadEvent.class, e -> {
			for(Tile tile : Vars.world.tiles){
				if(tile.overlay() instanceof LimitedOre ore){
					if (ore.getOreCount(tile) != 0) continue;
					ore.seedRandom(tile);
				}
			}
		});

		Events.on(WaveEvent.class, e -> {
			int wave = Vars.state.wave;
			if (wave % 10 == 0) {
				Log.info("cold: " + FrostMethods.cold.env());
				Vars.state.rules.attributes.set(FrostMethods.cold, FrostMethods.cold.env() + 0.1f);
				Vars.state.envAttrs.set(FrostMethods.cold, FrostMethods.cold.env() + 0.1f);
			}
		});

	};
}

