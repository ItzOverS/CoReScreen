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
import java.util.HashMap;
import java.util.List;

public class PacketHandler extends PacketListenerAbstract {
    private final boolean commandWhitelistEnabled = CoReScreen.getInstance().getConfig().getBoolean("settings.freeze.whitelist-commands.enabled"),
            commandWhitelistOverrideTabComplete = CoReScreen.getInstance().getConfig().getBoolean("settings.freeze.whitelist-commands.override-tabcomplete");
    private final List<String> whitelistCommands = CoReScreen.getInstance().getConfig().getStringList("settings.freeze.whitelist-commands.commands");
    private final HashMap<String, String> text = new HashMap<>();

    @Override
    public void onPacketPlayReceive(PacketPlayReceiveEvent e) {
        if(e.getPlayer().hasPermission("corescreen.freeze.commandbypass")) return;
        if(commandWhitelistEnabled && commandWhitelistOverrideTabComplete && e.getPacketId() == PacketType.Play.Client.TAB_COMPLETE && FreezeManager.isFrozen(e.getPlayer()))
            text.put(e.getPlayer().getName(), new WrappedPacketInTabComplete(e.getNMSPacket()).getText());
        else if(commandWhitelistEnabled && e.getPacketId() == PacketType.Play.Client.CHAT && FreezeManager.isFrozen(e.getPlayer())){
            WrappedPacketInChat packet = new WrappedPacketInChat(e.getNMSPacket());
            if(!packet.getMessage().startsWith("/")) return;
            if(!whitelistCommands.contains(packet.getMessage().substring(1).split(" ")[0])) e.setCancelled(true);
        }
    }

    @Override
    public void onPacketPlaySend(PacketPlaySendEvent e) {
        if(e.getPlayer().hasPermission("corescreen.freeze.commandbypass")) return;
        if(commandWhitelistEnabled && commandWhitelistOverrideTabComplete && e.getPacketId() == PacketType.Play.Server.TAB_COMPLETE && FreezeManager.isFrozen(e.getPlayer())){
            WrappedPacketOutTabComplete packet = new WrappedPacketOutTabComplete(e.getNMSPacket());
            if(text.get(e.getPlayer().getName()).split(" ").length > 1) return;
            packet.setMatches(whitelistCommands.stream().filter(r -> r.startsWith(text.get(e.getPlayer().getName()))).toArray(String[]::new));
        }
    }
}
