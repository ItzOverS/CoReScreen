package me.overlight.corescreen.Analyzer.Analyzers;

import io.github.retrooper.packetevents.PacketEvents;
import me.overlight.corescreen.Analyzer.AnalyzeModule;
import me.overlight.corescreen.CoReScreen;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Ping extends AnalyzeModule {
    public Ping() {
        super("Ping");
    }

    private final HashMap<String, Integer> minPing = new HashMap<>(), maxPing = new HashMap<>();
    private final HashMap<String, List<Integer>> averagePing = new HashMap<>();

    @Override
    public void activate(Player target) {
        averagePing.put(target.getName(), new ArrayList<>());
        new BukkitRunnable(){
            @Override
            public void run(){
                if(CoReScreen.getPlayer(target.getName()) == null) {
                    cancel();
                    return;
                }
                int ping = PacketEvents.get().getPlayerUtils().getPing(target);
                averagePing.get(target.getName()).add(ping);
                if(minPing.getOrDefault(target.getName(), Integer.MAX_VALUE) > ping) minPing.put(target.getName(), ping);
                if(maxPing.getOrDefault(target.getName(), 0) < ping) maxPing.put(target.getName(), ping);
            }
        }.runTaskTimerAsynchronously(CoReScreen.getInstance(), 1, 1);
    }

    @Override
    public void result(Player player, Player target) {
        send(player, "Min Ping", String.valueOf(minPing.get(target.getName())));
        send(player, "Max Ping", String.valueOf(maxPing.get(target.getName())));
        send(player, "Average", String.valueOf((int)averagePing.get(target.getName()).stream().mapToInt(r -> r).average().getAsDouble()));

        minPing.remove(target.getName());
        maxPing.remove(target.getName());
        averagePing.remove(target.getName());
    }
}
