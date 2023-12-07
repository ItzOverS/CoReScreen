package me.overlight.corescreen.Profiler.Profiles;

import me.overlight.corescreen.Profiler.Profiler;
import me.overlight.corescreen.Profiler.ProfilingSystem;
import org.bukkit.entity.Player;

public class Sprinting extends ProfilingSystem {
    public Sprinting() {
        super("sprint", "sprinting", Profiler.ProfileOption.sprinting);
    }

    @Override
    public String valueRequest(Player player, String text) {
        return text.replace("%placeholder%", String.valueOf(player.isSprinting()));
    }
}
