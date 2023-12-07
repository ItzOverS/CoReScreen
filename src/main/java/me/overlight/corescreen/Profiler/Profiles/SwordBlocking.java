package me.overlight.corescreen.Profiler.Profiles;

import me.overlight.corescreen.Profiler.Profiler;
import me.overlight.corescreen.Profiler.ProfilingSystem;
import org.bukkit.entity.Player;

public class SwordBlocking extends ProfilingSystem {
    public SwordBlocking() {
        super("swblocking", "sword-blocking", Profiler.ProfileOption.blocking);
    }

    @Override
    public String valueRequest(Player player, String text) {
        return text.replace("%placeholder%", String.valueOf(player.isBlocking()));
    }
}
