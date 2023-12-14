package me.overlight.corescreen.ClientSettings.Modules;

import me.overlight.corescreen.ClientSettings.CSModule;
import me.overlight.corescreen.ClientSettings.ClientSettingsGrabber;
import org.bukkit.entity.Player;

public class Locale extends CSModule {
    public Locale() {
        super("Locale", "locale");
    }

    @Override
    public String getValue(Player player) {
        return ClientSettingsGrabber.getSettings(player).getLocale();
    }
}
