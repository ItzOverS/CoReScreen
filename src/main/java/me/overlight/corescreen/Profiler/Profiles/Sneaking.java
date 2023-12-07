package me.overlight.corescreen.Profiler.Profiles;

import me.overlight.corescreen.Profiler.Profiler;
import me.overlight.corescreen.Profiler.ProfilingSystem;
import org.bukkit.entity.Player;

public class Sneaking extends ProfilingSystem {
    public Sneaking() {
        super("sneak", "sneaking", Profiler.ProfileOption.sneaking);
    }

    @Override
    public String valueRequest(Player player, String text) {
        return text.replace("%placeholder%", String.valueOf(player.isSneaking()));
    }
}
