package me.overlight.corescreen.Freeze.Cache;

import me.overlight.corescreen.CoReScreen;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CacheManager {
    public final static boolean isEnabled = CoReScreen.getInstance().getConfig().getBoolean("settings.freeze.cache.enabled");
    public final static int maxSize = CoReScreen.getInstance().getConfig().getInt("settings.freeze.cache.max-size");
    private final static HashMap<String, List<String>> cache = new HashMap<>();

    public static void cache(Player who, String data){
        if(!isEnabled || cache.getOrDefault(who.getName(), new ArrayList<>()).size() > maxSize) return;
        if(!cache.containsKey(who.getName())) cache.put(who.getName(), new ArrayList<>());
        cache.get(who.getName()).add(ChatColor.stripColor(data));
        if(cache.get(who.getName()).size() > maxSize) cache.get(who.getName()).add("Cache size length has limit to " + maxSize);
    }

    public static void saveCache(Player who){
        if(!isEnabled || !cache.containsKey(who.getName())) return;
        if(!new File("plugins\\CoReScreen\\freeze-cache").exists()) new File("plugins\\CoReScreen\\freeze-cache").mkdirs();
        File path = new File(CoReScreen.getInstance().getDataFolder(), "freeze-cache\\" + who.getName() + "." + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH.mm.ss.SSSS")) + ".txt");
        try {
            Files.createFile(Paths.get(path.getPath()));
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
                cache.get(who.getName()).forEach(r -> {
                    try {
                        writer.write(r + "\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        cache.remove(who.getName());
    }
}
