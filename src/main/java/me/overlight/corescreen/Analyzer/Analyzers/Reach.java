package me.overlight.corescreen.Analyzer.Analyzers;

import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.play.in.useentity.WrappedPacketInUseEntity;
import me.overlight.corescreen.Analyzer.AnalyzeModule;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class Reach extends AnalyzeModule {
    public Reach() {
        super("Reach");
    }

    private final HashMap<String, Double> minReach = new HashMap<>(), maxReach = new HashMap<>();

    @Override
    public void packetEvent(PacketPlayReceiveEvent e) {
        if(e.getPacketId() == PacketType.Play.Client.USE_ENTITY){
            WrappedPacketInUseEntity packet = new WrappedPacketInUseEntity(e.getNMSPacket());
            if(packet.getEntity() == null) return;
            if(packet.getAction() != WrappedPacketInUseEntity.EntityUseAction.ATTACK) return;
            double reach = e.getPlayer().getLocation().distance(packet.getEntity().getLocation()) - .3333333333f;
            if(minReach.getOrDefault(e.getPlayer().getName(), Double.MAX_VALUE) > reach) minReach.put(e.getPlayer().getName(), reach);
            if(maxReach.getOrDefault(e.getPlayer().getName(), 0D) < reach) maxReach.put(e.getPlayer().getName(), reach);
        }
    }

    @Override
    public void result(Player player, Player target) {
        send(player, "Min Reach", String.valueOf(minReach.getOrDefault(target.getName(), -1D)).substring(0, 7));
        send(player, "Max Reach", String.valueOf(maxReach.getOrDefault(target.getName(), -1D)).substring(0, 7));
        clearVariables(target);
    }
}
