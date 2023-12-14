package me.overlight.corescreen.ClientSettings;

import org.bukkit.entity.Player;

public class CSModule {
    private final String name, permission;

    public String getName() {
        return name;
    }

    public String getPermission() {
        return permission;
    }

    public CSModule(String name, String permission) {
        this.permission = permission;
        this.name = name;
    }

    public String getValue(Player player) {
        return null;
    }
}
