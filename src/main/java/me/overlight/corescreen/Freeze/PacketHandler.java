package me.overlight.corescreen.Freeze;

import io.github.retrooper.packetevents.event.PacketListenerAbstract;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.event.impl.PacketPlaySendEvent;
import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.play.in.chat.WrappedPacketInChat;
import io.github.retrooper.packetevents.packetwrappers.play.in.tabcomplete.WrappedPacketInTabComplete;
import io.github.retrooper.packetevents.packetwrappers.play.out.tabcomplete.WrappedPacketOutTabComplete;
import me.overlight.corescreen.CoReScreen;

import java.util.ArrayList;
import java.util.List;

public class PacketHandler extends PacketListenerAbstract {
    private final boolean commandWhitelistEnabled = CoReScreen.getInstance().getConfig().getBoolean("settings.freeze.whitelist-commands.enabled"),
            commandWhitelistOverrideTabComplete = CoReScreen.getInstance().getConfig().getBoolean("settings.freeze.whitelist-commands.override-tabcomplete");
    private final List<String> whitelistCommands = CoReScreen.getInstance().getConfig().getStringList("settings.freeze.whitelist-commands.commands");
    private String text = "";

    @Override
    public void onPacketPlayReceive(PacketPlayReceiveEvent e) {
        if(commandWhitelistEnabled && commandWhitelistOverrideTabComplete && e.getPacketId() == PacketType.Play.Client.TAB_COMPLETE)
            text = new WrappedPacketInTabComplete(e.getNMSPacket()).getText();
        else if(commandWhitelistEnabled && e.getPacketId() == PacketType.Play.Client.CHAT){
            WrappedPacketInChat packet = new WrappedPacketInChat(e.getNMSPacket());
            if(!packet.getMessage().startsWith("/")) return;
            if(!whitelistCommands.contains(packet.getMessage().substring(1).split(" ")[0])) e.setCancelled(true);
        }
    }

    @Override
    public void onPacketPlaySend(PacketPlaySendEvent e) {
        if(commandWhitelistEnabled && commandWhitelistOverrideTabComplete && e.getPacketId() == PacketType.Play.Server.TAB_COMPLETE){
            WrappedPacketOutTabComplete packet = new WrappedPacketOutTabComplete(e.getNMSPacket());
            if(text.split(" ").length > 1) return;
            packet.setMatches(whitelistCommands.stream().filter(r -> r.startsWith(text)).toArray(String[]::new));
        }
    }
}
