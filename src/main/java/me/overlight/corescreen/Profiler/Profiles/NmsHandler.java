package me.overlight.corescreen.Profiler.Profiles;

import me.overlight.corescreen.CoReScreen;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class NmsHandler {
    public final static List<NmsWrapper> handlers = new ArrayList<>();

    public static String handleNMS(Player who, NmsWrapper packet) {
        try {
            Object CraftPlayer = who.getClass().getMethod("getHandle").invoke(who);
            return CraftPlayer.getClass().getField(packet.getPath()).get(CraftPlayer).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "INVALIDATE_ARGUMENT";
        }
    }

    public static void loadCustomSettings(){
        ConfigurationSection section = CoReScreen.getInstance().getConfig().getConfigurationSection("settings.profiler.formats.player-handler");
        handlers.addAll(new ArrayList<>(section.getKeys(false).stream().map(r -> new NmsWrapper(section.getString(r + ".path"), section.getString(r + ".label"), r)).collect(Collectors.toList())));
    }

    public static class NmsWrapper {
        private final String path, label, name;

        public NmsWrapper(String path, String label, String name) {
            this.path = path;
            this.label = label;
            this.name = name;
        }

        public String getPath() {
            return path;
        }

        public String getLabel() {
            return label;
        }

        public String getName() {
            return name;
        }
    }

    public static class NmsProfiler {
        public List<NmsWrapper> nmsWrapper;
        private final String staff, who;

        public NmsProfiler(String staff, String who, List<NmsWrapper> nmsWrapper) {
            this.nmsWrapper = nmsWrapper;
            this.staff = staff;
            this.who = who;
        }

        public String getStaff() {
            return staff;
        }

        public String getWho() {
            return who;
        }
    }
}
