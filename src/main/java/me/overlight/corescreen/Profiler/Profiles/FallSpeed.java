package me.overlight.corescreen.Profiler.Profiles;

import me.overlight.corescreen.Profiler.Profiler;
import me.overlight.corescreen.Profiler.ProfilingSystem;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;

public class FallSpeed extends ProfilingSystem implements Listener {
    public FallSpeed() {
        super("fallspeed", "fall-speed", Profiler.ProfileOption.fallspeed);
    }

    private final HashMap<String, Double> speed = new HashMap<>();
    private final HashMap<String, Long> deltaSpeed = new HashMap<>();

    @Override
    public String valueRequest(Player player, String text) {
        return text.replace("%placeholder%", System.currentTimeMillis() - deltaSpeed.getOrDefault(player.getName(), 0L) < 500 ? String.valueOf(speed.getOrDefault(player.getName(), 0D)) : "0");
    }

    @EventHandler
    public void event(PlayerMoveEvent e) {
        if (!isProfiling(e.getPlayer())) return;
        if (e.getTo().getY() < e.getFrom().getY()) {
            speed.put(e.getPlayer().getName(), e.getFrom().getY() - e.getTo().getY());
            deltaSpeed.put(e.getPlayer().getName(), System.currentTimeMillis());
        }
    }

    @Override
    public void stopHandling(String player) {
        speed.remove(player);
        deltaSpeed.remove(player);
    }
}
