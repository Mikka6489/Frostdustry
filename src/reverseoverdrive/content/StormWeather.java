package frostdustry.content;

import frostdustry.type.weather.*;
import mindustry.type.*;

public class StormWeather{
    public static Weather Storm;

    public static void load() {
        Storm = new Storm("Storm");
    }
}
