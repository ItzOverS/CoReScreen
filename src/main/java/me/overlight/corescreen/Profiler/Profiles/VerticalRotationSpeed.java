package me.overlight.corescreen.Profiler.Profiles;

import me.overlight.corescreen.Profiler.Profiler;
import me.overlight.corescreen.Profiler.ProfilingSystem;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;

public class VerticalRotationSpeed extends ProfilingSystem implements Listener {
    public VerticalRotationSpeed() {
        super("vrotation", "vertical-rotation-speed", Profiler.ProfileOption.vrot);
    }

    private final HashMap<String, Float> speed = new HashMap<>();
    private final HashMap<String, Long> deltaSpeed = new HashMap<>();
    private final HashMap<String, Float> delta = new HashMap<>();

    @Override
    public String valueRequest(Player player, String text) {
        return text.replace("%placeholder%", System.currentTimeMillis() - deltaSpeed.getOrDefault(player.getName(), 0L) < 500 ? String.valueOf(speed.getOrDefault(player.getName(), -1f)) : "0");
    }

    @EventHandler
    public void event(PlayerMoveEvent e) {
        if (!isProfiling(e.getPlayer())) return;
        if (delta.containsKey(e.getPlayer().getName()) && delta.get(e.getPlayer().getName()) != e.getPlayer().getLocation().getPitch()) {
            speed.put(e.getPlayer().getName(), getAngleDistance(e.getPlayer().getLocation().getPitch(), delta.get(e.getPlayer().getName())));
            deltaSpeed.put(e.getPlayer().getName(), System.currentTimeMillis());
        }
        delta.put(e.getPlayer().getName(), e.getPlayer().getLocation().getPitch());
    }

    private float getAngleDistance(float alpha, float beta) {
        final float phi = Math.abs(beta - alpha) % 360;
        return phi > 180 ? 360 - phi : phi;
    }

    @Override
    public void stopHandling(String player) {
        speed.remove(player);
        deltaSpeed.remove(player);
        delta.remove(player);
    }
}
