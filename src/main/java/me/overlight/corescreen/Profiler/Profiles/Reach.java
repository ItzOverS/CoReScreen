package me.overlight.corescreen.Profiler.Profiles;

import me.overlight.corescreen.Profiler.Profiler;
import me.overlight.corescreen.Profiler.ProfilingSystem;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.HashMap;

public class Reach extends ProfilingSystem implements Listener {
    public Reach() {
        super("reach", "reach", Profiler.ProfileOption.reach);
    }

    private final HashMap<String, Double> reach = new HashMap<>();
    private final HashMap<String, Long> lastReach = new HashMap<>();

    @Override
    public String valueRequest(Player player, String text) {
        return text.replace("%placeholder%", System.currentTimeMillis() - lastReach.getOrDefault(player.getName(), 0L) < 3000 ? String.valueOf(reach.getOrDefault(player.getName(), 0D)) : "-");
    }

    @EventHandler
    public void event(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player)) return;
        if (!isProfiling((Player) e.getDamager())) return;
        reach.put(e.getDamager().getName(), e.getDamager().getLocation().distance(e.getEntity().getLocation()) - .33f);
        lastReach.put(e.getDamager().getName(), System.currentTimeMillis());
    }

    @Override
    public void stopHandling(String player) {
        reach.remove(player);
        lastReach.remove(player);
    }
}
