package frostdustry.content;

import frostdustry.type.weather.*;
import mindustry.type.*;

public class FrostWeather{
    public static Weather Storm;

    public static void load() {
        Storm = new Storm("Storm");
    }
}
