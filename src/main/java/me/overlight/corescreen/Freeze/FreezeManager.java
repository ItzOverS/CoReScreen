package me.overlight.corescreen.Freeze;

import io.github.retrooper.packetevents.event.PacketListenerAbstract;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.packettype.PacketType;
import me.overlight.corescreen.CoReScreen;
import me.overlight.corescreen.Freeze.Warps.WarpManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FreezeManager {
    private final static List<String> frozen = new ArrayList<String>();
    public final static List<String> freezeWhenLogin = new ArrayList<String>();
    public final static HashMap<String, Location> lastGround = new HashMap<>();
    public final static boolean blindEye = CoReScreen.getInstance().getConfig().getBoolean("settings.freeze.blind-eye.enabled");
    public static boolean isFrozen(Player player) {
        return frozen.contains(player.getName());
    }

    public static void freezePlayer(Player player) {
        frozen.add(player.getName());
        player.setWalkSpeed(0);
        if(blindEye) player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 1, false, false));
    }

    public static void unfreezePlayer(Player player) {
        frozen.remove(player.getName());
        player.setWalkSpeed(.2f);
        if(blindEye) player.removePotionEffect(PotionEffectType.BLINDNESS);
        WarpManager.warpPlayerToLast(player);
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
