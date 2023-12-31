package me.overlight.corescreen.Vanish;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.packetwrappers.play.out.entitydestroy.WrappedPacketOutEntityDestroy;
import io.github.retrooper.packetevents.packetwrappers.play.out.playerinfo.WrappedPacketOutPlayerInfo;
import me.overlight.corescreen.CoReScreen;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.*;

import java.util.Objects;
import java.util.stream.Collectors;

public class Listeners implements Listener {
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void event(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player && VanishManager.isVanish((Player) e.getDamager()) && !e.getDamager().hasPermission("corescreen.vanish.permission.damageother")) e.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void event(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player && VanishManager.isVanish((Player) e.getEntity()) && !e.getEntity().hasPermission("corescreen.vanish.permission.damageself")) e.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void event(EntityShootBowEvent e) {
        if (e.getEntity() instanceof Player && VanishManager.isVanish((Player) e.getEntity()) && !e.getEntity().hasPermission("corescreen.vanish.permission.usebow")) e.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void event(BlockPlaceEvent e) {
        if (VanishManager.isVanish(e.getPlayer()) && !e.getPlayer().hasPermission("corescreen.vanish.permission.blockplace")) e.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void event(BlockBreakEvent e) {
        if (VanishManager.isVanish(e.getPlayer()) && !e.getPlayer().hasPermission("corescreen.vanish.permission.blockbreak")) e.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void event(PlayerInteractEvent e) {
        if (VanishManager.isVanish(e.getPlayer()) && !e.getPlayer().hasPermission("corescreen.vanish.permission.interactblock")) e.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void event(PlayerInteractEntityEvent e) {
        if (VanishManager.isVanish(e.getPlayer()) && !e.getPlayer().hasPermission("corescreen.vanish.permission.interactentity")) e.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void event(PlayerInteractAtEntityEvent e) {
        if (VanishManager.isVanish(e.getPlayer()) && !e.getPlayer().hasPermission("corescreen.vanish.permission.interactentity")) e.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void event(PlayerItemConsumeEvent e) {
        if (VanishManager.isVanish(e.getPlayer()) && !e.getPlayer().hasPermission("corescreen.vanish.permission.consume")) e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void event(PlayerJoinEvent e) {
        if (!e.getPlayer().hasPermission(PacketHandler.see_other_permission)) {
            Bukkit.getScheduler().runTask(CoReScreen.getInstance(), () ->
                    VanishManager.vanishes.forEach(o -> {
                        if (o.equals(e.getPlayer().getName())) return;
                        Player vanished = null;
                        try {
                            vanished = Bukkit.getOnlinePlayers().stream().filter(p -> p.getName().equals(o)).collect(Collectors.toList()).get(0);
                        } catch (Exception ignored) {
                        }
                        if (vanished != null) {
                            Player finalVanished = vanished;
                            Bukkit.getScheduler().runTask(CoReScreen.getInstance(), () -> PacketEvents.get().getPlayerUtils().sendPacket(e.getPlayer(), new WrappedPacketOutEntityDestroy(finalVanished.getEntityId())));
                            Bukkit.getScheduler().runTask(CoReScreen.getInstance(), () -> PacketEvents.get().getPlayerUtils().sendNMSPacket(e.getPlayer(), new WrappedPacketOutPlayerInfo(WrappedPacketOutPlayerInfo.PlayerInfoAction.REMOVE_PLAYER, getPlayerInfo(finalVanished))));
                        }
                    })
            );
        }
        if (VanishManager.isVanish(e.getPlayer())) {
            e.setJoinMessage(null);
            Bukkit.getScheduler().runTask(CoReScreen.getInstance(), () -> {
                Bukkit.getOnlinePlayers().stream().filter(r -> !r.hasPermission(PacketHandler.see_other_permission) && !Objects.equals(r.getName(), e.getPlayer().getName())).forEach(r -> {
                    Bukkit.getScheduler().runTask(CoReScreen.getInstance(), () -> PacketEvents.get().getPlayerUtils().sendPacket(r, new WrappedPacketOutPlayerInfo(WrappedPacketOutPlayerInfo.PlayerInfoAction.REMOVE_PLAYER, getPlayerInfo(e.getPlayer()))));
                    Bukkit.getScheduler().runTask(CoReScreen.getInstance(), () -> PacketEvents.get().getPlayerUtils().sendPacket(r, new WrappedPacketOutEntityDestroy(e.getPlayer().getEntityId())));
                });
            });

            Bukkit.getScheduler().runTask(CoReScreen.getInstance(), () -> e.getPlayer().sendMessage(CoReScreen.translate("messages.vanish.game.join-vanish")));
        }
    }

    @EventHandler
    public void event(PlayerQuitEvent e){
        if(VanishManager.isVanish(e.getPlayer())) e.setQuitMessage(null);
    }

    private WrappedPacketOutPlayerInfo.PlayerInfo getPlayerInfo(Player player){
        return new WrappedPacketOutPlayerInfo.PlayerInfo(player.getName(), PacketEvents.get().getPlayerUtils().getGameProfile(player), player.getGameMode(), PacketEvents.get().getPlayerUtils().getPing(player));
    }
}
