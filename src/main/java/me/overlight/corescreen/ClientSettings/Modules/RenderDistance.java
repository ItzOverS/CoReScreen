package me.overlight.corescreen.ClientSettings.Modules;

import me.overlight.corescreen.ClientSettings.CSModule;
import me.overlight.corescreen.ClientSettings.ClientSettingsGrabber;
import org.bukkit.entity.Player;

public class RenderDistance extends CSModule {
    public RenderDistance() {
        super("RenderDistance", "renderdistance");
    }

    @Override
    public String getValue(Player player) {
        return String.valueOf(ClientSettingsGrabber.getSettings(player).getRenderDistance());
    }
}
