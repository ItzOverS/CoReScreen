package me.overlight.corescreen.Vanish.Land;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.Serializable;

public class DataPacket implements Serializable {

    private final Class plugin;
    private final String label;
    private final Object data;

    public DataPacket(JavaPlugin plugin, String label, Object data) {
        this.plugin = plugin.getClass();
        this.label = label;
        this.data = data;
    }

    public Class getPlugin() {
        return plugin;
    }

    public String getLabel() {
        return label;
    }

    public Object getData() {
        return data;
    }
}
