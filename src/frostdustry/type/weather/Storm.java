package frostdustry.type.weather;

import arc.util.*;
import mindustry.gen.*;
import mindustry.type.weather.*;
import mindustry.world.meta.*;

public class Storm extends ParticleWeather {

    public Storm(String name){
        super(name);
        useWindVector = true;
        drawNoise = true;
        noiseLayerAlphaM = 0.25f;
        opacityMultiplier = 1f;
        sizeMin = 2f;
        sizeMax = 18f;
        minAlpha = 1f;
        maxAlpha = 1f;
        density = 2600f;
        baseSpeed = 2.4f;
        force = 0.1f;
        sound = Sounds.wind;
        soundVol = 0.5f;
        duration = 7f * Time.toMinutes;
        attrs.set(Attribute.light, 0.3f);
    }
}

