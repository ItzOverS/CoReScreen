package me.overlight.corescreen.Freeze.Warps;

import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class Listener implements org.bukkit.event.Listener {
    @EventHandler
    public void event(PlayerQuitEvent e){
        WarpManager.warpPlayerToLast(e.getPlayer());
    }

    @EventHandler(ignoreCancelled = true)
    public void event(BlockBreakEvent e){
        if(WarpManager.warps.stream().anyMatch(r -> r.playerName.equals(e.getPlayer().getName()) && r.inUse)) e.setCancelled(true);
    }
    @EventHandler(ignoreCancelled = true)
    public void event(BlockPlaceEvent e){
        if(WarpManager.warps.stream().anyMatch(r -> r.playerName.equals(e.getPlayer().getName()) && r.inUse)) e.setCancelled(true);
    }
    @EventHandler(ignoreCancelled = true)
    public void event(PlayerItemConsumeEvent e){
        if(WarpManager.warps.stream().anyMatch(r -> r.playerName.equals(e.getPlayer().getName()) && r.inUse)) e.setCancelled(true);
    }
    @EventHandler(ignoreCancelled = true)
    public void event(PlayerInteractEvent e){
        if(WarpManager.warps.stream().anyMatch(r -> r.playerName.equals(e.getPlayer().getName()) && r.inUse)) e.setCancelled(true);
    }
    @EventHandler(ignoreCancelled = true)
    public void event(PlayerInteractAtEntityEvent e){
        if(WarpManager.warps.stream().anyMatch(r -> r.playerName.equals(e.getPlayer().getName()) && r.inUse)) e.setCancelled(true);
    }
}
