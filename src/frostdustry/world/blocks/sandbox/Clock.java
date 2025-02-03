package frostdustry.world.blocks.sandbox;

import arc.util.*;
import mindustry.world.blocks.*;
import mindustry.world.meta.*;

import frostdustry.content.*;
import frostdustry.world.*;

public class Clock extends FrostBlock {

    public float cooldown;
    public float countdown = 60f;
    public float indextimer;

    public Clock(String name){
        super(name);
        update = true;
        canBeHeated = false;
    }

    public class ClockBuilding extends FrostBuilding {

        public final Attribute coldattr = FrostAttribute.cold;
        public Attributes attrs = new Attributes();

        public void updateTile(){

            this.attrs.set(this.coldattr, 0);
            cooldown += 1 * Time.delta;

            if (cooldown >= countdown){
                indextimer++;
                cooldown = 0f;
                Log.info("clock timer = " + indextimer);

                if (indextimer > 59){
                    indextimer = 0f;
                }
            }
        }
    }
}
