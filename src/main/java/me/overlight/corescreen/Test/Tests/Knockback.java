package me.overlight.corescreen.Test.Tests;

import me.overlight.corescreen.CoReScreen;
import me.overlight.corescreen.Test.TestCheck;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class Knockback extends TestCheck {
    public Knockback() {
        super("Knockback", new String[]{"knockback", "kb"});
    }

    public final static List<String> inTest = new ArrayList<>();

    private static double minAvg, maxAvg, MotionX, MotionY, MotionZ;
    private static int delay, rounds, sDelay;

    static {
        Bukkit.getScheduler().runTask(CoReScreen.getInstance(), () -> {
            minAvg = CoReScreen.getInstance().getConfig().getDouble("settings.test.knockback.average-check.min");
            maxAvg = CoReScreen.getInstance().getConfig().getDouble("settings.test.knockback.average-check.max");
            MotionX = CoReScreen.getInstance().getConfig().getDouble("settings.test.knockback.motion.x");
            MotionY = CoReScreen.getInstance().getConfig().getDouble("settings.test.knockback.motion.y");
            MotionZ = CoReScreen.getInstance().getConfig().getDouble("settings.test.knockback.motion.z");
            delay = CoReScreen.getInstance().getConfig().getInt("settings.test.knockback.delay");
            sDelay = Math.min(CoReScreen.getInstance().getConfig().getInt("settings.test.knockback.setback-delay"), delay - 1);
            rounds = CoReScreen.getInstance().getConfig().getInt("settings.test.knockback.rounds");
        });
    }

    @Override
    public void handle(Player player, Player executor) {
        executor.sendMessage(CoReScreen.translate("messages.test.knockback.perform").replace("%player%", player.getName()));
        inTest.add(player.getName());
        new BukkitRunnable() {
            int round;
            final List<Double> nums = new ArrayList<>();

            @Override
            public void run() {
                if (Bukkit.getOnlinePlayers().stream().noneMatch(r -> r.getName().equals(player.getName())) || !player.isOnline()) {
                    cancel();
                    return;
                }
                Location pos = player.getLocation().clone().clone();
                player.setVelocity(new Vector(MotionX, MotionY, MotionZ));
                Bukkit.getScheduler().runTaskLater(CoReScreen.getInstance(), () -> {
                    if (Bukkit.getOnlinePlayers().stream().noneMatch(r -> r.getName().equals(player.getName())) || !player.isOnline()) return;
                    nums.add(pos.distance(player.getLocation()));
                    player.setFallDistance(0f);
                    player.teleport(pos.clone());
                    if (round >= rounds) {
                        player.setVelocity(new Vector(0, 0, 0));
                        double avg = nums.stream().mapToDouble(r -> r).average().getAsDouble();
                        if (minAvg < avg && avg < maxAvg) executor.sendMessage(CoReScreen.translate("messages.test.knockback.success").replace("%player%", player.getName()));
                        else executor.sendMessage(CoReScreen.translate("messages.test.knockback.failure").replace("%player%", player.getName()));
                        Bukkit.getConsoleSender().sendMessage("Knockback test applied on " + player.getName() + ". Average Velocity was " + avg);
                        inTest.remove(player.getName());
                    }
                }, sDelay);
                round++;
                if (round >= rounds) {
                    cancel();
                }
            }
        }.runTaskTimer(CoReScreen.getInstance(), 0, delay);
    }
}
