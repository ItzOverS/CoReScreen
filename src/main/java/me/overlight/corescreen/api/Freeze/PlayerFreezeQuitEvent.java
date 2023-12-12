package me.overlight.corescreen.api.Freeze;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerFreezeQuitEvent extends Event implements Cancellable{
    public static HandlerList list = new HandlerList();

    private boolean cancelled = false;

    public Player getTarget() {
        return target;
    }

    private final Player target;

    public PlayerFreezeQuitEvent(boolean cancelled, Player target) {
        this.target = target;
    }

    @Override
    public HandlerList getHandlers() {
        return list;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        cancelled = b;
    }
}
