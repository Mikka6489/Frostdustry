package frostdustry.logic;

import arc.*;
import arc.scene.*;
import arc.scene.ui.layout.*;
import mindustry.*;
import mindustry.game.EventType.*;
import mindustry.ui.*;

public class UIHandler {
    public static void load(){
        Events.on(ClientLoadEvent.class, event -> {
            Element element = Core.scene.find("statustable");
            if(!(element instanceof Table)){
                throw new IllegalStateException("Could not find the wave counter HUD table.");
            }
            Table table = (Table)element;

            table.row();
            table.table(countdown -> {
                countdown.defaults().pad(0f);
                countdown.image(Core.atlas.find("frostdustry-thermometer")).size(64f).pad(10f);
                countdown.label(() -> Integer.toString(wavesUntilTemperatureDrop())).style(Styles.outlineLabel);
            }).left().colspan(2).pad(0f);
        });
    }

    private static int wavesUntilTemperatureDrop(){
        return 10 - Vars.state.wave % 10;
    }
}
