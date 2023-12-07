package me.overlight.corescreen.Profiler.Profiles;

import me.overlight.corescreen.CoReScreen;
import me.overlight.corescreen.Profiler.Profiler;
import me.overlight.corescreen.Profiler.ProfilingSystem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;

public class MovementSpeed extends ProfilingSystem implements Listener {
    private final static HashMap<String, Double> movementSpeed = new HashMap<>();
    private final static HashMap<String, Location> lastPos = new HashMap<>();
    private final static HashMap<String, Integer> maxV = new HashMap<>();

    public MovementSpeed() {
        super("mspeed", "movement-speed", Profiler.ProfileOption.movementspeed);
        Bukkit.getScheduler().runTaskTimer(CoReScreen.getInstance(), () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (lastPos.containsKey(player.getName())) {
                    if (lastPos.get(player.getName()).toVector().setY(0).distance(player.getLocation().toVector().setY(0)) == 0) {
                        maxV.put(player.getName(), maxV.getOrDefault(player.getName(), 0) + 1);
                        if (maxV.get(player.getName()) > 5) {
                            movementSpeed.put(player.getName(), 0.);
                        }
                    } else {
                        maxV.remove(player.getName());
                    }
                }
                lastPos.put(player.getName(), player.getLocation().clone());
            }
        }, 1, 1);
    }

    @EventHandler
    public void event(PlayerMoveEvent e) {
        movementSpeed.put(e.getPlayer().getName(), e.getFrom().toVector().setY(0).distance(e.getTo().toVector().setY(0)) * 20f);
    }

    @Override
    public void stopHandling(String player) {
        movementSpeed.remove(player);
        maxV.remove(player);
        lastPos.remove(player);
    }

    @Override
    public String valueRequest(Player player, String text) {
        return text.replace("%placeholder%", String.valueOf(getSpeed(player)));
    }

    private static double getSpeed(Player who) {
        return movementSpeed.getOrDefault(who.getName(), -1.);
    }
}
