package me.overlight.corescreen.Profiler;

import org.bukkit.entity.Player;

public class ProfilingSystem {
    private final String name, placeholder;
    private final Profiler.ProfileOption mode;

    public boolean isProfiling(Player player) {
        return ProfilerManager.profilers.stream().anyMatch(r -> r.getWho().equalsIgnoreCase(player.getName()) && r.options.contains(mode));
    }

    public Profiler.ProfileOption getMode() {
        return mode;
    }

    public String getName() {
        return name;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public ProfilingSystem(String name, String placeholder, Profiler.ProfileOption mode) {
        this.name = name;
        this.placeholder = placeholder;
        this.mode = mode;
    }

    public String valueRequest(Player player, String text) {
        return null;
    }

    public void startHandling(Player player) {

    }

    public void stopHandling(String player) {

    }
}
