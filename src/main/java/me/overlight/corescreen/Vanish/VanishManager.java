package me.overlight.corescreen.Vanish;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.play.out.entitydestroy.WrappedPacketOutEntityDestroy;
import me.overlight.corescreen.CoReScreen;
import me.overlight.corescreen.Profiler.ProfilerManager;
import me.overlight.powerlib.Chat.Text.impl.PlayerActionBar;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
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
            Bukkit.getScheduler().runTask(CoReScreen.getInstance(), () -> PacketEvents.get().getPlayerUtils().sendNMSPacket(p, new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, ((CraftPlayer)player).getHandle())));
            EntityTracker tracker = ((WorldServer)((CraftPlayer) p).getHandle().world).tracker;
            EntityPlayer other = ((CraftPlayer)player).getHandle();
            EntityTrackerEntry entry = tracker.trackedEntities.get(other.getId());
            if (entry != null) {
                entry.clear(((CraftPlayer) p).getHandle());
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
                PacketEvents.get().getPlayerUtils().sendNMSPacket(p,
                        new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER,
                                ((CraftPlayer) player).getHandle()));
                PacketEvents.get().getPlayerUtils().sendNMSPacket(p,
                        new PacketPlayOutNamedEntitySpawn(((CraftPlayer) player).getHandle()));
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
}
