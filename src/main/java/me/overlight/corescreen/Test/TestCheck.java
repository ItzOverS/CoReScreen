package me.overlight.corescreen.Test;

import org.bukkit.entity.Player;

public class TestCheck {
    private final String name;
    private final String[] alias;

    public String getName() {
        return name;
    }

    public String[] getAlias() {
        return alias;
    }

    public TestCheck(String name, String[] alias) {
        this.name = name;
        this.alias = alias;
    }

    public void handle(Player player, Player executor) {

    }
}
