package me.overlight.corescreen.Test.Tests;

import me.overlight.corescreen.CoReScreen;
import me.overlight.corescreen.Test.TestCheck;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Random;

public class Rotation extends TestCheck {
    public Rotation() {
        super("Rotation", new String[]{"rotate", "rotation", "rot"});
    }

    @Override
    public void handle(Player player, Player executor) {
        executor.sendMessage(CoReScreen.translate("messages.test.rotate.perform").replace("%player%", player.getName()));
        final Location updateRot = player.getLocation().clone();
        updateRot.setYaw(new Random().nextInt(360) - 180);
        updateRot.setPitch(new Random().nextInt(180) - 90);
        player.teleport(updateRot);
        Bukkit.getScheduler().runTaskLater(CoReScreen.getInstance(), () -> {
            if (Bukkit.getOnlinePlayers().stream().noneMatch(r -> r.getName().equals(player.getName())) || !player.isOnline()) return;
            if (player.getLocation().getYaw() != updateRot.getYaw() || player.getLocation().getPitch() != updateRot.getPitch()) executor.sendMessage(CoReScreen.translate("messages.test.rotate.failure").replace("%player%", player.getName()));
            else executor.sendMessage(CoReScreen.translate("messages.test.rotate.success").replace("%player%", player.getName()));
        }, 3);
    }
}
