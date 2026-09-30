package frostdustry.content;

import mindustry.type.*;

import frostdustry.type.weather.*;

public class FrostWeather{
    public static Weather Storm;

    public static void load() {
        Storm = new Storm("Storm");
    }
}
