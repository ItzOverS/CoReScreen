package me.overlight.corescreen.ClientSettings.Modules;

import io.github.retrooper.packetevents.PacketEvents;
import me.overlight.corescreen.ClientSettings.CSModule;
import org.bukkit.entity.Player;

public class ClientVersion extends CSModule {
    public ClientVersion() {
        super("ClientVersion", "clientversion");
    }

    @Override
    public String getValue(Player player) {
        return PacketEvents.get().getPlayerUtils().getClientVersion(player).name().substring(2).replace("_", ".");
    }
}
