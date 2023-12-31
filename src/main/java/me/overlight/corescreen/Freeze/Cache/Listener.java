package me.overlight.corescreen.Freeze.Cache;

import io.github.retrooper.packetevents.event.PacketListenerAbstract;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.play.in.chat.WrappedPacketInChat;
import me.overlight.corescreen.CoReScreen;
import me.overlight.corescreen.Freeze.FreezeManager;

public class Listener extends PacketListenerAbstract {
    @Override
    public void onPacketPlayReceive(PacketPlayReceiveEvent e) {
        if(e.getPacketId() == PacketType.Play.Client.CHAT){
            String cmd = new WrappedPacketInChat(e.getNMSPacket()).getMessage();
            if(cmd.startsWith("/")) {
                if(e.getPlayer().hasPermission("corescreen.freeze.cache")) FreezeManager.frozen.forEach(r -> CacheManager.cache(CoReScreen.getPlayer(r), "[STAFF] " + e.getPlayer().getName() + " has executed command: " + cmd));
                else if(FreezeManager.isFrozen(e.getPlayer())) CacheManager.cache(e.getPlayer(), "[FROZEN] " + e.getPlayer().getName() + " has executed command: " + cmd);
            } else {
                if(e.getPlayer().hasPermission("corescreen.freeze.cache")) FreezeManager.frozen.forEach(r -> CacheManager.cache(CoReScreen.getPlayer(r), "[STAFF] " + e.getPlayer().getName() + " has sent chat message: " + cmd));
                else if(FreezeManager.isFrozen(e.getPlayer())) CacheManager.cache(e.getPlayer(), "[FROZEN] " + e.getPlayer().getName() + " has sent chat message: " + cmd);
            }
        }
    }
}
