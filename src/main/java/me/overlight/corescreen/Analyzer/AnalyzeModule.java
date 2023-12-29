package me.overlight.corescreen.Analyzer;

import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.event.impl.PacketPlaySendEvent;
import me.overlight.corescreen.CoReScreen;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;

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

    public void packetEvent(PacketPlayReceiveEvent e) { }
    public void packetEvent(PacketPlaySendEvent e) { }

    public void clearVariables(Player target){
        Field[] fields = getClass().getFields();
        Arrays.stream(fields).forEach(r -> {
            if(r.getType() == HashMap.class){
                try {
                    r.setAccessible(true);
                    HashMap<String, ?> map = (HashMap<String, ?>) r.get(getClass());
                    map.remove(target.getName());
                    r.setAccessible(false);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
