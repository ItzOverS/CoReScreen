package me.overlight.corescreen.Freeze;

import me.overlight.corescreen.CoReScreen;
import me.overlight.corescreen.Commands;
import me.overlight.corescreen.DiscordWebhook;
import me.overlight.powerlib.Chat.Text.impl.PlayerChatMessage;
import me.overlight.powerlib.Chat.Text.impl.ext.ClickableCommand;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Listeners implements Listener {
    @EventHandler
    public void event(AsyncPlayerChatEvent e) {
        if (FreezeManager.isFrozen(e.getPlayer())) {
            e.getRecipients().removeIf(r -> !r.hasPermission("corescreen.freeze.chat") && !Objects.equals(e.getPlayer().getName(), r.getName()));
        }
    }

    @EventHandler
    public void event(PlayerQuitEvent e) {
        if (FreezeManager.isFrozen(e.getPlayer())) {
            FreezeManager.unfreezePlayer(e.getPlayer());
            Bukkit.getOnlinePlayers().stream().filter(p -> p.hasPermission("corescreen.freeze.alert")).forEach(p -> {
                p.sendMessage(CoReScreen.translate("messages.freeze.game.other-logout.message").replace("%who%", e.getPlayer().getName()));
                List<BaseComponent> components = new ArrayList<>();
                for (String k : CoReScreen.getInstance().getConfig().getStringList("messages.freeze.game.other-logout.action-message")) {
                    if (k.startsWith("%")) {
                        ConfigurationSection section = CoReScreen.getInstance().getConfig().getConfigurationSection("messages.freeze.game.other-logout.actions." + k.replace("%", "").trim());
                        if (section != null) {
                            components.add(new PlayerChatMessage(ChatColor.translateAlternateColorCodes('&', section.getString("content")
                                    .replace("%who%", e.getPlayer().getName())
                                    .replace("%prefix%", Commands.prefix == null ? "&e&lCoRe&cVanish &6»" : Commands.prefix)))
                                    .click(new ClickableCommand(section.getString("command").replace("%who%", e.getPlayer().getName())))
                                    .hover(ChatColor.translateAlternateColorCodes('&', section.getString("hover").replace("%who%", e.getPlayer().getName()))).getComponent());
                        }
                    } else {
                        components.add(new PlayerChatMessage(ChatColor.translateAlternateColorCodes('&', k.replace("%prefix%", Commands.prefix))).getComponent());
                    }
                }
                p.spigot().sendMessage(components.toArray(new BaseComponent[0]));
            });
            DiscordWebhook.createDef(CoReScreen.getInstance().getConfig().getString("discord.webhooks.on-freeze-logout")).setContent("**" + e.getPlayer().getName() + "** has logout while frozen!").execute();
        }
    }

    @EventHandler
    public void event(PlayerJoinEvent e) {
        if (FreezeManager.freezeWhenLogin.contains(e.getPlayer().getName())) {
            Bukkit.getScheduler().runTaskLater(CoReScreen.getInstance(), () -> {
                FreezeManager.freezePlayer(e.getPlayer());
                FreezeManager.freezeWhenLogin.remove(e.getPlayer().getName());
                Bukkit.getOnlinePlayers().stream().filter(p -> p.hasPermission("corescreen.freeze.alert")).forEach(p ->
                        p.sendMessage(CoReScreen.translate("messages.freeze.command.other-login-and-freeze-again").replace("%who%", e.getPlayer().getName())));
            }, 20);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void event(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player && FreezeManager.isFrozen((Player) e.getEntity())) e.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void event(EntityShootBowEvent e) {
        if (e.getEntity() instanceof Player && FreezeManager.isFrozen((Player) e.getEntity())) e.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void event(BlockBreakEvent e) {
        if (FreezeManager.isFrozen(e.getPlayer())) e.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void event(BlockPlaceEvent e) {
        if (FreezeManager.isFrozen(e.getPlayer())) e.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void event(PlayerInteractEvent e) {
        if (FreezeManager.isFrozen(e.getPlayer())) e.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void event(PlayerInteractEntityEvent e) {
        if (FreezeManager.isFrozen(e.getPlayer())) e.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void event(PlayerInteractAtEntityEvent e) {
        if (FreezeManager.isFrozen(e.getPlayer())) e.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void event(PlayerItemConsumeEvent e) {
        if (FreezeManager.isFrozen(e.getPlayer())) e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void event(PlayerMoveEvent e) {
        if (!FreezeManager.isFrozen(e.getPlayer())) {
            if (e.getPlayer().isOnGround()) FreezeManager.lastGround.put(e.getPlayer().getName(), e.getPlayer().getLocation());
        }
        if (!FreezeManager.isFrozen(e.getPlayer())) return;
        Location lastOnGround = FreezeManager.lastGround.get(e.getPlayer().getName());
        if (lastOnGround == null) {
            lastOnGround = e.getPlayer().getLocation().clone();
            for (int i = lastOnGround.getBlockY(); i > 0; i--)
                if (lastOnGround.add(0, i, 0).getBlock().getType() != Material.AIR) {
                    lastOnGround.setY(i);
                    break;
                }
        }
        if (e.getPlayer().getLocation().distance(lastOnGround) > .01f) {
            final Location loc = lastOnGround.clone();
            loc.setPitch(e.getPlayer().getLocation().getPitch());
            loc.setYaw(e.getPlayer().getLocation().getYaw());
            e.getPlayer().teleport(loc);
        }
    }
}
