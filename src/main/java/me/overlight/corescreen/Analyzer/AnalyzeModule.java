package me.overlight.corescreen.Analyzer;

import me.overlight.corescreen.CoReScreen;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public class AnalyzeModule {
    public String getName() {
        return name;
    }

    private final String name;

    public AnalyzeModule(String name) {
        this.name = name;
    }

    public void activate(Player target){

    }

    public void result(Player player, Player target){

    }

    public void send(Player player, String key, String value){
        player.sendMessage("§9§l" + key + "§7: §e" + value);
    }
}
