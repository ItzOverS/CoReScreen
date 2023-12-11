package me.overlight.corescreen.Freeze;

import io.github.retrooper.packetevents.event.PacketListenerAbstract;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.packettype.PacketType;
import me.overlight.corescreen.CoReScreen;
import me.overlight.corescreen.Freeze.Cache.CacheManager;
import me.overlight.corescreen.Freeze.Warps.WarpManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FreezeManager {
    public final static List<String> frozen = new ArrayList<String>();
    public final static List<String> freezeWhenLogin = new ArrayList<String>();
    public final static HashMap<String, HashMap<Integer, ItemStack>> inventory = new HashMap<>();
    public final static HashMap<String, Location> lastGround = new HashMap<>();
    public final static boolean blindEye = CoReScreen.getInstance().getConfig().getBoolean("settings.freeze.blind-eye.enabled");
    public final static boolean clearInv = CoReScreen.getInstance().getConfig().getBoolean("settings.freeze.inventory-clearing.enabled");
    public static boolean isFrozen(Player player) {
        return frozen.contains(player.getName());
    }

    public static void freezePlayer(Player player) {
        frozen.add(player.getName());
        player.setWalkSpeed(0);
        if(blindEye) player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 1, false, false));
        if(clearInv){
            inventory.put(player.getName(), new HashMap<>());
            for(int i = 0; i < 36; i++){
                if(player.getInventory().getItem(i) == null) continue;
                inventory.get(player.getName()).put(i, player.getInventory().getItem(i));
            }
            inventory.get(player.getName()).put(-10, player.getInventory().getHelmet());
            inventory.get(player.getName()).put(-9, player.getInventory().getChestplate());
            inventory.get(player.getName()).put(-8, player.getInventory().getLeggings());
            inventory.get(player.getName()).put(-7, player.getInventory().getBoots());
            player.getInventory().setArmorContents(new ItemStack[]{ null, null, null, null });
            player.getInventory().clear();
        }
    }

    public static void unfreezePlayer(Player player) {
        frozen.remove(player.getName());
        player.setWalkSpeed(.2f);
        if(blindEye) player.removePotionEffect(PotionEffectType.BLINDNESS);
        WarpManager.warpPlayerToLast(player);
        CacheManager.saveCache(player);
        if(clearInv){
            for(int i: inventory.getOrDefault(player.getName(), new HashMap<>()).keySet()){
                if(i < 0) continue;
                player.getInventory().setItem(i, inventory.get(player.getName()).get(i));
            }
            player.getInventory().setHelmet(inventory.get(player.getName()).get(-10));
            player.getInventory().setChestplate(inventory.get(player.getName()).get(-9));
            player.getInventory().setLeggings(inventory.get(player.getName()).get(-8));
            player.getInventory().setBoots(inventory.get(player.getName()).get(-7));
            inventory.remove(player.getName());
        }
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
