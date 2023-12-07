package me.overlight.corescreen;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.overlight.corescreen.Vanish.VanishManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Placeholders extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "crs";
    }

    @Override
    public @NotNull String getAuthor() {
        return "_OverLight_";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String args) {
        if (args.equalsIgnoreCase("onlineplayers")) {
            return String.valueOf(Bukkit.getOnlinePlayers().size() - VanishManager.vanishes.stream().filter(p -> Bukkit.getOnlinePlayers().stream().filter(r -> r.getName().equals(p)).count() == 1).count());
        } else if (args.equalsIgnoreCase("vanishplayers")) {
            return String.valueOf(VanishManager.vanishes.size());
        } else if (args.equalsIgnoreCase("isvanish")) {
            return String.valueOf(VanishManager.isVanish(player));
        }
        return null;
    }
}
