package me.overlight.corescreen.Test.Tests;

import me.overlight.corescreen.CoReScreen;
import me.overlight.corescreen.Test.TestCheck;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Random;

public class Shuffle extends TestCheck {
    public Shuffle() {
        super("Shuffle", new String[]{"shuffle"});
    }

    @Override
    public void handle(Player player, Player executor) {
        executor.sendMessage(CoReScreen.translate("messages.test.shuffle.perform").replace("%player%", player.getName()));
        final int shuffle = new Random().nextInt(9);
        player.getInventory().setHeldItemSlot(shuffle);
        Bukkit.getScheduler().runTaskLater(CoReScreen.getInstance(), () -> {
            if (player.getInventory().getHeldItemSlot() != shuffle) executor.sendMessage(CoReScreen.translate("messages.test.shuffle.failure").replace("%player%", player.getName()));
            else executor.sendMessage(CoReScreen.translate("messages.test.shuffle.success").replace("%player%", player.getName()));
        }, 3);
    }
}
