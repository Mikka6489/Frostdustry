package frostdustry.content;

import arc.graphics.*;
import mindustry.game.*;
import mindustry.graphics.g3d.*;
import mindustry.maps.planet.*;
import mindustry.type.*;
import mindustry.world.meta.*;
import mindustry.world.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.ctype.*;

public class FrostPlanet {
    public static Planet frostLand;
    public static void load() {
        Block raggedyStorage = Vars.content.getByName(ContentType.block, "frostdustry-raggedy-storage");
        frostLand = new Planet("frostLand", Planets.sun, 1f, 1){{
            generator = new TantrosPlanetGenerator();
            meshLoader = () -> new HexMesh(this, 5);
            cloudMeshLoader = () -> new MultiMesh(
                new HexSkyMesh(this, 2, 0.15f, 0.14f, 5, Color.valueOf("eba768").a(0.75f), 2, 0.42f, 1f, 0.43f),
                new HexSkyMesh(this, 3, 0.6f, 0.15f, 5, Color.valueOf("eea293").a(0.75f), 2, 0.42f, 1.2f, 0.45f)
            );
            alwaysUnlocked = true;
            landCloudColor = Color.valueOf("4984b8");
            atmosphereColor = Color.valueOf("7492b9");
            defaultEnv = Env.terrestrial;
            startSector = 10;
            atmosphereRadIn = 0.02f;
            atmosphereRadOut = 0.3f;
            tidalLock = true;
            orbitSpacing = 5f;
            totalRadius += 2.6f;
            lightSrcTo = 0.5f;
            lightDstFrom = 0.2f;
            clearSectorOnLose = true;
            defaultCore = raggedyStorage;
            iconColor = Color.valueOf("ff9266");
            enemyBuildSpeedMultiplier = 0.4f;
            allowLaunchToNumbered = false;
            updateLighting = false;

            defaultAttributes.set(Attribute.heat, 0.8f);

            ruleSetter = r -> {
                r.waveTeam = Team.malis;
                r.placeRangeCheck = false;
//                r.hideSpawns = false;
                r.fog = false;
                r.staticFog = false;
                r.lighting = false;
                r.coreDestroyClear = true;
                r.onlyDepositCore = true;
            };
            campaignRuleDefaults.fog = false;
//            campaignRuleDefaults.hideSpawns = false;
            campaignRuleDefaults.rtsAI = true;

        }};
    }
}
