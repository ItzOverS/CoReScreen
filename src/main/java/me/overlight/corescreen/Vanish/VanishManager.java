package me.overlight.corescreen.Vanish;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.packetwrappers.play.out.entitydestroy.WrappedPacketOutEntityDestroy;
import io.github.retrooper.packetevents.packetwrappers.play.out.namedentityspawn.WrappedPacketOutNamedEntitySpawn;
import io.github.retrooper.packetevents.packetwrappers.play.out.playerinfo.WrappedPacketOutPlayerInfo;
import me.overlight.corescreen.CoReScreen;
import me.overlight.corescreen.Profiler.ProfilerManager;
import me.overlight.powerlib.Chat.Text.impl.PlayerActionBar;
import me.overlight.powerlib.ServerData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class VanishManager {
    public static final List<String> vanishes = new ArrayList<>();

    static {
        Bukkit.getScheduler().runTaskTimer(CoReScreen.getInstance(), () -> {
            new PlayerActionBar(CoReScreen.translate("messages.vanish.game.action-bar")).send(
                    vanishes.stream().filter(r -> Bukkit.getOnlinePlayers().stream().anyMatch(f -> f.getName().equals(r))
                            && !ProfilerManager.isProfiling(r)).map(r -> Bukkit.getOnlinePlayers().stream().filter(f -> f.getName().equals(r)).collect(Collectors.toList()).get(0)).toArray(Player[]::new));
            vanishes.stream().filter(r -> Bukkit.getOnlinePlayers().stream().anyMatch(f -> f.getName().equals(r))).map(r -> Bukkit.getOnlinePlayers().stream().filter(f -> f.getName().equals(r))
                    .collect(Collectors.toList()).get(0)).forEach(r -> {
                r.setAllowFlight(true);
            });
        }, 10, 20);
    }

    public static boolean isVanish(Player player) {
        if(player == null) return false;
        return vanishes.contains(player.getName());
    }

    public static void vanishPlayer(Player player) {
        if (isVanish(player)) return;
        if (CoReScreen.getInstance().getConfig().getString("messages.vanish.game.quit-message", null) != null && !CoReScreen.translate("messages.vanish.game.quit-message").trim().isEmpty())
            Bukkit.getOnlinePlayers().forEach(p -> p.sendMessage(CoReScreen.translate("messages.vanish.game.quit-message").replace("%player%", player.getName())));
        Bukkit.getOnlinePlayers().stream().filter(p -> !p.hasPermission(PacketHandler.see_other_permission) && !p.getName().equals(player.getName())).forEach(p -> {
            Bukkit.getScheduler().runTask(CoReScreen.getInstance(), () -> PacketEvents.get().getPlayerUtils().sendPacket(p, new WrappedPacketOutEntityDestroy(player.getEntityId())));
            try {
                Class<?> EnumPlayerInfoAction = getNMS("PacketPlayOutPlayerInfo$EnumPlayerInfoAction"), EntityPlayer = getNMS("EntityPlayer");
                Object entities = Array.newInstance(EntityPlayer, 1);
                Array.set(entities, 0, player.getClass().getMethod("getHandle").invoke(player));
                PacketEvents.get().getPlayerUtils().sendNMSPacket(p, getNMS("PacketPlayOutPlayerInfo").getDeclaredConstructor(EnumPlayerInfoAction, Array.newInstance(EntityPlayer, 1).getClass()).newInstance(EnumPlayerInfoAction.getEnumConstants()[4], entities));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        vanishes.add(player.getName());
        player.setAllowFlight(true);
        BackwardServerMessenger.sendData("enable", player.getName());
    }

    public static void vanishOffline(String name) {
        if (vanishes.contains(name.trim())) return;
        vanishes.add(name.trim());
    }

    public static void unVanishOffline(String name) {
        vanishes.remove(name.trim());
    }

    public static void unVanishPlayer(Player player) {
        if (!isVanish(player)) return;
        vanishes.remove(player.getName());
        if (CoReScreen.getInstance().getConfig().getString("messages.vanish.game.join-message", null) != null && !CoReScreen.getInstance().getConfig().getString("messages.vanish.game.join-message", null).trim().isEmpty())
            Bukkit.getOnlinePlayers().forEach(p -> p.sendMessage(CoReScreen.translate("messages.vanish.game.join-message").replace("%player%", player.getName())));
        Bukkit.getOnlinePlayers().stream().filter(p -> !p.hasPermission(PacketHandler.see_other_permission) && !p.getName().equals(player.getName())).forEach(p -> {
            Bukkit.getScheduler().runTask(CoReScreen.getInstance(), () -> {
                try {
                    Class<?> EnumPlayerInfoAction = getNMS("PacketPlayOutPlayerInfo$EnumPlayerInfoAction"), EntityPlayer = getNMS("EntityPlayer");
                    Object entities = Array.newInstance(EntityPlayer, 1);
                    Array.set(entities, 0, player.getClass().getMethod("getHandle").invoke(player));
                    PacketEvents.get().getPlayerUtils().sendNMSPacket(p, getNMS("PacketPlayOutPlayerInfo").getDeclaredConstructor(EnumPlayerInfoAction, Array.newInstance(EntityPlayer, 1).getClass()).newInstance(EnumPlayerInfoAction.getEnumConstants()[0], entities));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                PacketEvents.get().getPlayerUtils().sendPacket(p,
                        new WrappedPacketOutNamedEntitySpawn(player));
            });
        });
        player.setAllowFlight(false);
        player.setFlying(false);
        BackwardServerMessenger.sendData("disable", player.getName());
    }

    public static void toggleVanish(Player player) {
        if (isVanish(player)) unVanishPlayer(player);
        else vanishPlayer(player);
    }

    private static Class getNMS(String nms){
        try {
            return Class.forName("net.minecraft.server." + Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3] + "." + nms);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }
}
