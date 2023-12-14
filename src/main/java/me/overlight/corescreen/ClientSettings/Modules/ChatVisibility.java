package me.overlight.corescreen.ClientSettings.Modules;

import io.github.retrooper.packetevents.PacketEvents;
import me.overlight.corescreen.ClientSettings.CSModule;
import me.overlight.corescreen.ClientSettings.ClientSettingsGrabber;
import org.bukkit.entity.Player;

public class ChatVisibility extends CSModule {
    public ChatVisibility() {
        super("ChatVisibility", "chatvisibility");
    }

    @Override
    public String getValue(Player player) {
        return ClientSettingsGrabber.getSettings(player).getChatVisibility().name();
    }
}
