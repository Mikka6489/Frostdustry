package frostdustry.logic;

import arc.*;
import arc.scene.*;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import arc.util.*;
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
                Label overlay = new Label("", Styles.outlineLabel);
                overlay.setText(() -> calcTemperature() + "°C");
                overlay.setAlignment(Align.center);
                countdown.stack(
                    new Image(Core.atlas.find("frostdustry-thermometer")),
                    overlay
                ).size(64f).pad(10f);
                countdown.label(() -> "Waves: " + wavesUntilTemperatureDrop()).style(Styles.outlineLabel);
            }).left().colspan(2).pad(0f);
        });
    }

    private static int calcTemperature(){
        return Math.round(((FrostMethods.cold.env() * -1) + 1f) * 10);
    }

    private static int wavesUntilTemperatureDrop(){
        return FrostMethods.coldDropPerWave - Vars.state.wave % FrostMethods.coldDropPerWave;
    }
}
