package me.overlight.corescreen.Profiler;

import me.overlight.corescreen.CoReScreen;
import me.overlight.corescreen.Profiler.Profiles.NmsHandler;
import me.overlight.powerlib.Chat.Text.impl.PlayerActionBar;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class ProfileHandler
        implements Runnable {

    private final static List<String> handlers = new ArrayList<>();

    @Override
    public void run() {
        HashMap<String, String> selections = new HashMap<>();
        HashMap<String, List<Profiler.ProfileOption>> options = new HashMap<>();
        HashMap<String, List<NmsHandler.NmsWrapper>> optionsNms = new HashMap<>();
        for (Profiler profiler : ProfilerManager.profilers) {
            selections.put(profiler.getStaff(), profiler.getWho());
            options.put(profiler.getStaff(), profiler.options);
        }
        for (NmsHandler.NmsProfiler profiler : ProfilerManager.profilersNms) {
            selections.put(profiler.getStaff(), profiler.getWho());
            optionsNms.put(profiler.getStaff(), profiler.nmsWrapper);
        }
        for (String k : selections.keySet()) {
            List<String> m = new ArrayList<>();
            Player who, staff;
            try {
                who = Bukkit.getOnlinePlayers().stream().filter(r -> r.getName().equals(selections.get(k))).collect(Collectors.toList()).get(0);
                staff = Bukkit.getOnlinePlayers().stream().filter(r -> r.getName().equals(k)).collect(Collectors.toList()).get(0);
            } catch (Exception ex) {
                for (Profiler.ProfileOption v : options.get(k)) {
                    ProfilingSystem profiler = ProfilerManager.profilingSystems.stream().filter(r -> r.getMode().equals(v)).collect(Collectors.toList()).get(0);
                    profiler.stopHandling(selections.get(k));
                }
                handlers.remove(selections.get(k));
                ProfilerManager.removeProfiler(k);
                continue;
            }
            if(options.containsKey(k)) {
                for (Profiler.ProfileOption v : options.get(k)) {
                    ProfilingSystem profiler = ProfilerManager.profilingSystems.stream().filter(r -> r.getMode().equals(v)).collect(Collectors.toList()).get(0);
                    if (!handlers.contains(who.getName())) {
                        handlers.add(who.getName());
                        profiler.startHandling(who);
                    }
                    m.add(profiler.valueRequest(who, CoReScreen.translate("settings.profiler.formats." + profiler.getPlaceholder())));
                }
            }
            if(optionsNms.containsKey(k)) {
                for (NmsHandler.NmsWrapper v : optionsNms.get(k)) {
                    m.add(ChatColor.translateAlternateColorCodes('&', v.getLabel().replace("%placeholder%", NmsHandler.handleNMS(who, v))));
                }
            }
            new PlayerActionBar((CoReScreen.getInstance().getConfig().getBoolean("settings.profiler.show-target") ? CoReScreen.translate("settings.profiler.formats.player") : "").replace("%player%", who.getName()) +
                    String.join(CoReScreen.translate("settings.profiler.formats.splitter"), m)).send(staff);
        }
    }
}
