package me.overlight.corescreen.Profiler.Profiles;

import me.overlight.corescreen.Profiler.Profiler;
import me.overlight.corescreen.Profiler.ProfilingSystem;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class CPS extends ProfilingSystem implements Listener {
    private final static HashMap<String, List<Long>> lmb = new HashMap<>();
    private final static HashMap<String, List<Long>> rmb = new HashMap<>();

    public CPS() {
        super("cps", "cps", Profiler.ProfileOption.cps);
    }

    @EventHandler
    public void event(PlayerInteractEvent e) {
        if (!isProfiling(e.getPlayer())) return;
        if (Arrays.asList(Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK).contains(e.getAction())) {
            rmb.get(e.getPlayer().getName()).add(System.currentTimeMillis());
        } else if (Arrays.asList(Action.LEFT_CLICK_AIR, Action.LEFT_CLICK_BLOCK).contains(e.getAction())) {
            lmb.get(e.getPlayer().getName()).add(System.currentTimeMillis());
        }
    }

    @Override
    public void stopHandling(String player) {
        rmb.remove(player);
        lmb.remove(player);
    }

    @Override
    public void startHandling(Player player) {
        lmb.put(player.getName(), new ArrayList<>());
        rmb.put(player.getName(), new ArrayList<>());
    }

    @Override
    public String valueRequest(Player player, String text) {
        return text.replace("%placeholder_2%", String.valueOf(getRMB(player))).replace("%placeholder_1%", String.valueOf(getLMB(player)));
    }

    private static int getRMB(Player who) {
        rmb.getOrDefault(who.getName(), new ArrayList<>()).removeIf(p -> System.currentTimeMillis() - p > 1000);
        return rmb.getOrDefault(who.getName(), new ArrayList<>()).size();
    }

    private static int getLMB(Player who) {
        lmb.getOrDefault(who.getName(), new ArrayList<>()).removeIf(p -> System.currentTimeMillis() - p > 1000);
        return lmb.getOrDefault(who.getName(), new ArrayList<>()).size();
    }
}
