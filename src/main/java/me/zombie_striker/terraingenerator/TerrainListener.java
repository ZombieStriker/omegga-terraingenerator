package me.zombie_striker.terraingenerator;

import me.zombie_striker.omeggajava.JOmegga;
import me.zombie_striker.omeggajava.events.ChatEvent;
import me.zombie_striker.omeggajava.events.EventHandler;
import me.zombie_striker.omeggajava.events.Listener;
import me.zombie_striker.omeggajava.events.TickEvent;

public class TerrainListener implements Listener {

    private int tickcount = 0;
    private int chunksLoaded = 0;
    private boolean start = false;

    @EventHandler
    public void onChatEvent(ChatEvent event) {
        if (event.getMessage().startsWith("!gen")) {
            String[] messages = event.getMessage().split(" ");
            if (messages.length < 3) {
                if (event.getMessage().startsWith("!gen start")) {
                    start = !start;
                    JOmegga.broadcast("Setting terrain gen to " + start);
                    return;
                }
            }
            JOmegga.whisper(event.getPlayername(),"Command Usage: !gen start");
        }
    }

    @EventHandler
    public void onTick(TickEvent event) {
        tickcount++;
        if (tickcount > 2) {
            if (!start) {
                return;
            }
            tickcount = 0;
            if(chunksLoaded > (long)JOmegga.getConfig().get("maxchunks")){
                JOmegga.broadcast("Generation done ");
                start = false;
                return;
            }
            int[] xy = SprialXY.getXYForSpiral(chunksLoaded);
            Main.manager.generate(xy[0],xy[1]);
            chunksLoaded++;
        }
    }
}
