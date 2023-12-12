package me.overlight.corescreen.api.Freeze;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerFreezeEvent extends Event implements Cancellable {
    public static HandlerList list = new HandlerList();

    private boolean cancelled = false;

    public Player getTarget() {
        return target;
    }

    public Player getStaff() {
        return staff;
    }

    private final Player target, staff;

    public PlayerFreezeEvent(boolean cancelled, Player target, Player staff) {
        this.cancelled = cancelled;
        this.target = target;
        this.staff = staff;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        cancelled = b;
    }

    @Override
    public HandlerList getHandlers() {
        return list;
    }
}
