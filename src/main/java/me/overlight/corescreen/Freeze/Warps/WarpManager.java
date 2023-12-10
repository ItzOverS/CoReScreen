package me.overlight.corescreen.Freeze.Warps;

import me.overlight.corescreen.CoReScreen;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class WarpManager {
    private static FileConfiguration config = CoReScreen.getInstance().getConfig();
    public final static boolean isEnabled = config.getBoolean("settings.freeze.warp.enabled") &&
            config.getConfigurationSection("settings.freeze.warp.warps").getKeys(false).size() > 0,
        teleportBack = config.getBoolean("settings.freeze.warp.teleport-back"),
        protectWarp = config.getBoolean("settings.freeze.warp.protect-warp");
    public final static List<Warp> warps = new ArrayList<>(config.getConfigurationSection("settings.freeze.warp.warps").getKeys(false)).stream().map(r -> {
        String l = config.getConfigurationSection("settings.freeze.warp.warps").getString(r);
        Float[] v = Arrays.stream(l.split(";")).collect(Collectors.toList()).subList(1, l.split(";").length).stream().map(Float::valueOf).toArray(Float[]::new);
        return new Warp(new Location(Bukkit.getWorld(l.split(";")[0]), v[0], v[1], v[2], v[3], v[4]), r);
    }).collect(Collectors.toList());

    private final static HashMap<String, Location> teleports = new HashMap<>();

    public static boolean warpPlayerToEmpty(Player player){
        if(!isEnabled) return false;
        boolean canWarp = warps.stream().anyMatch(r -> !r.inUse);
        if(!canWarp) return false;
        Warp warp = warps.stream().filter(r -> !r.inUse).findAny().get();
        warp.inUse = true;
        warp.playerName = player.getName();
        if(teleportBack) teleports.put(player.getName(), player.getLocation());
        player.teleport(warp.location.clone());
        return true;
    }

    public static void warpPlayerToLast(Player player){
        if(!isEnabled) return;
        if(teleportBack && !teleports.containsKey(player.getName())) {
            player.kickPlayer("No Last Known Position Detected For You!");
            return;
        }
        if(teleportBack) {
            player.teleport(teleports.get(player.getName()));
            teleports.remove(player.getName());
        }
        Warp warp = warps.stream().filter(r -> !r.inUse).findAny().get();
        warp.playerName = null;
        warp.inUse = false;
    }

    public static class Warp {
        public Location location;
        public String name, playerName;
        public boolean inUse = false;

        public Warp(Location location, String name) {
            this.location = location;
            this.name = name;
        }
    }
}
