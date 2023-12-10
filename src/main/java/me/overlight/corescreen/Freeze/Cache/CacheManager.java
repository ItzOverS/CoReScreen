package me.overlight.corescreen.Freeze.Cache;

import me.overlight.corescreen.CoReScreen;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
        if(!isEnabled || cache.get(who.getName()).size() > maxSize) return;
        if(!cache.containsKey(who.getName())) cache.put(who.getName(), new ArrayList<>());
        cache.get(who.getName()).add(ChatColor.stripColor(data));
        if(cache.get(who.getName()).size() > maxSize) cache.get(who.getName()).add("Cache size length has limit to " + maxSize);
    }

    public static void saveCache(Player who){
        if(!isEnabled || !cache.containsKey(who.getName())) return;
        File path = new File("plugins\\CoReScreen\\freeze-cache\\" + who.getName() + " - " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSSS")));
        try {
            path.createNewFile();
            FileWriter writer = new FileWriter(path);
            cache.get(who.getName()).forEach(r -> {
                try {
                    writer.write(r);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            writer.close();
            cache.remove(who.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
