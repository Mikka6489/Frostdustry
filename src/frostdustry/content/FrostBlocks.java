package frostdustry.content;

import mindustry.content.*;
import mindustry.entities.bullet.*;
import mindustry.entities.part.*;
import mindustry.entities.pattern.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.draw.*;

import frostdustry.world.blocks.defense.*;
import frostdustry.world.blocks.defense.turrets.*;
import frostdustry.world.blocks.production.*;
import frostdustry.world.blocks.tiles.*;

import static mindustry.type.ItemStack.*;

public class FrostBlocks{
    
    public static Block generator, frostdrill, graphiteClamp, refridgerator, heater, ohno, newLead, duo;

    public static void load() {


            duo = new FrostITurret("duo"){{
            requirements(Category.turret, with(Items.copper, 35));
            ammo(
                Items.copper,  new BasicBulletType(2.5f, 9){{
                    width = 7f;
                    height = 9f;
                    lifetime = 60f;
                    ammoMultiplier = 2;

                    hitEffect = despawnEffect = Fx.hitBulletColor;
                    hitColor = backColor = trailColor = Pal.copperAmmoBack;
                    frontColor = Pal.copperAmmoFront;
                }},
                Items.graphite, new BasicBulletType(3.5f, 18){{
                    width = 9f;
                    height = 12f;
                    ammoMultiplier = 4;
                    lifetime = 60f;
                    reloadMultiplier = 0.8f;
                    rangeChange = 16f;

                    hitEffect = despawnEffect = Fx.hitBulletColor;
                    hitColor = backColor = trailColor = Pal.graphiteAmmoBack;
                    frontColor = Pal.graphiteAmmoFront;
                }},
                Items.silicon, new BasicBulletType(3f, 12){{
                    width = 7f;
                    height = 9f;
                    homingPower = 0.2f;
                    reloadMultiplier = 1.5f;
                    ammoMultiplier = 5;
                    lifetime = 60f;

                    trailLength = 5;
                    trailWidth = 1.5f;

                    hitEffect = despawnEffect = Fx.hitBulletColor;
                    hitColor = backColor = trailColor = Pal.siliconAmmoBack;
                    frontColor = Pal.siliconAmmoFront;
                }}
            );

            shoot = new ShootAlternate(3.5f);

            recoils = 2;
            drawer = new DrawTurret(){{
                for(int i = 0; i < 2; i ++){
                    int f = i;
                    parts.add(new RegionPart("-barrel-" + (i == 0 ? "l" : "r")){{
                        progress = PartProgress.recoil;
                        recoilIndex = f;
                        under = true;
                        moveY = -1.5f;
                    }});
                }
            }};

            shootSound = Sounds.shootDuo;
            recoil = 0.5f;
            shootY = 3f;
            reload = 20f;
            range = 160;
            shootCone = 15f;
            ammoUseEffect = Fx.casing1;
            health = 250;
            inaccuracy = 2f;
            rotateSpeed = 10f;
            coolant = consumeCoolant(0.1f);
            coolantMultiplier = 10f;
            researchCostMultiplier = 0.05f;
//            depositCooldown = 2.0f;

            limitRange(5f);
        }};


        newLead = new FrostOre("ore-newLead", Items.lead);
        generator = new Generator("generator"){{
            requirements(Category.effect, with(Items.copper, 60, Items.sand, 15, Items.metaglass, 40));
            size = 5;
            health = 5000;
            hasItems = true;
            itemCapacity = 9000;
        }};

            frostdrill = new FrostDrill("frostdrill"){{
                requirements(Category.effect, with(Items.copper, 60, Items.sand, 15, Items.metaglass, 40));
                size = 2;
                health = 5000;
                tier = 4;
                drillTime = 600;
//                hasItems = true;
//                itemCapacity = 9000;
            }};

            graphiteClamp = new FrostGCrafter("graphite-clamp"){{
                requirements(Category.crafting, with(Items.copper, 75, Items.lead, 30));
                craftEffect = Fx.pulverizeMedium;
                outputItem = new ItemStack(Items.graphite, 1);
                craftTime = 90f;
                size = 2;
                hasItems = true;
                consumeItem(Items.coal, 2);
            }};

            heater = new Heater("heater"){{
                requirements(Category.effect, with(Items.lead, 100, Items.titanium, 75, Items.silicon, 75, Items.plastanium, 30));
                size = 2;
                itemCapacity = 0;
            }};
        };
    }
