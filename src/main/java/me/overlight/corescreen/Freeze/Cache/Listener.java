package me.overlight.corescreen.Freeze.Cache;

import io.github.retrooper.packetevents.event.PacketListenerAbstract;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.play.in.chat.WrappedPacketInChat;
import me.overlight.corescreen.Freeze.FreezeManager;
import me.overlight.powerlib.api.commands.CommandPreProcessEvent;
import org.bukkit.event.EventHandler;

public class Listener extends PacketListenerAbstract {
    @Override
    public void onPacketPlayReceive(PacketPlayReceiveEvent e) {
        if(e.getPacketId() == PacketType.Play.Client.CHAT){
            String cmd = new WrappedPacketInChat(e.getNMSPacket()).getMessage();
            if(cmd.startsWith("/")) {
                if(e.getPlayer().hasPermission("corescreen.freeze.cache")) CacheManager.cache(e.getPlayer(), "[STAFF] " + e.getPlayer().getName() + " has executed command: " + cmd);
                if(FreezeManager.isFrozen(e.getPlayer())) CacheManager.cache(e.getPlayer(), "[FROZEN] " + e.getPlayer().getName() + " has executed command: " + cmd);
            } else {
                if(e.getPlayer().hasPermission("corescreen.freeze.cache")) CacheManager.cache(e.getPlayer(), "[STAFF] " + e.getPlayer().getName() + " has sent chat message: " + cmd);
                if(FreezeManager.isFrozen(e.getPlayer())) CacheManager.cache(e.getPlayer(), "[FROZEN] " + e.getPlayer().getName() + " has sent chat message: " + cmd);
            }
        }
    }
}
