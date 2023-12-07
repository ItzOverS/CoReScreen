package me.overlight.corescreen.Profiler.Profiles;

import me.overlight.corescreen.Profiler.Profiler;
import me.overlight.corescreen.Profiler.ProfilingSystem;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class Ping extends ProfilingSystem {
    public Ping() {
        super("ping", "ping", Profiler.ProfileOption.ping);
    }

    @Override
    public String valueRequest(Player player, String text) {
        return text.replace("%placeholder%", String.valueOf(((CraftPlayer) player).getHandle().ping));
    }
}
