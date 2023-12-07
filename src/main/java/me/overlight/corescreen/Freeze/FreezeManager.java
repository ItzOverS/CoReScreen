package me.overlight.corescreen.Freeze;

import io.github.retrooper.packetevents.event.PacketListenerAbstract;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.packettype.PacketType;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FreezeManager {
    private final static List<String> frozen = new ArrayList<String>();
    public final static List<String> freezeWhenLogin = new ArrayList<String>();
    public final static HashMap<String, Location> lastGround = new HashMap<>();

    public static boolean isFrozen(Player player) {
        return frozen.contains(player.getName());
    }

    public static void freezePlayer(Player player) {
        frozen.add(player.getName());
        player.setWalkSpeed(0);
    }

    public static void unfreezePlayer(Player player) {
        frozen.remove(player.getName());
        player.setWalkSpeed(.2f);
    }

    public static void toggleFreeze(Player player) {
        if (isFrozen(player)) unfreezePlayer(player);
        else freezePlayer(player);
    }

    public static class Handler extends PacketListenerAbstract {
        @Override
        public void onPacketPlayReceive(PacketPlayReceiveEvent e) {
            if (!FreezeManager.isFrozen(e.getPlayer())) return;
            if (e.getPacketId() == PacketType.Play.Client.ARM_ANIMATION) e.setCancelled(true);
            if (e.getPacketId() == PacketType.Play.Client.USE_ENTITY) e.setCancelled(true);
        }
    }
}
