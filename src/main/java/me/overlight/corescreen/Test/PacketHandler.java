package me.overlight.corescreen.Test;

import io.github.retrooper.packetevents.event.PacketListenerAbstract;
import io.github.retrooper.packetevents.event.impl.PacketPlaySendEvent;
import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.play.out.entity.WrappedPacketOutEntity;
import io.github.retrooper.packetevents.packetwrappers.play.out.entityvelocity.WrappedPacketOutEntityVelocity;
import me.overlight.corescreen.Test.Tests.Knockback;
import org.bukkit.entity.Player;

public class PacketHandler extends PacketListenerAbstract {
    @Override
    public void onPacketPlaySend(PacketPlaySendEvent e) {
        if (e.getPacketId() == PacketType.Play.Server.ENTITY_VELOCITY) {
            WrappedPacketOutEntityVelocity packet = new WrappedPacketOutEntityVelocity(e.getNMSPacket());
            if (packet.getEntity() instanceof Player && Knockback.inTest.contains(packet.getEntity().getName()) && !packet.getEntity().getName().equals(e.getPlayer().getName())) e.setCancelled(true);
        } else if (e.getPacketId() == PacketType.Play.Server.ENTITY_LOOK) {
            WrappedPacketOutEntity.WrappedPacketOutEntityLook packet = new WrappedPacketOutEntity.WrappedPacketOutEntityLook(e.getNMSPacket());
            if (packet.getEntity() instanceof Player && Knockback.inTest.contains(packet.getEntity().getName()) && !packet.getEntity().getName().equals(e.getPlayer().getName())) e.setCancelled(true);
        } else if (e.getPacketId() == PacketType.Play.Server.REL_ENTITY_MOVE) {
            WrappedPacketOutEntity.WrappedPacketOutRelEntityMove packet = new WrappedPacketOutEntity.WrappedPacketOutRelEntityMove(e.getNMSPacket());
            if (packet.getEntity() instanceof Player && Knockback.inTest.contains(packet.getEntity().getName()) && !packet.getEntity().getName().equals(e.getPlayer().getName())) e.setCancelled(true);
        } else if (e.getPacketId() == PacketType.Play.Server.REL_ENTITY_MOVE_LOOK) {
            WrappedPacketOutEntity.WrappedPacketOutRelEntityMoveLook packet = new WrappedPacketOutEntity.WrappedPacketOutRelEntityMoveLook(e.getNMSPacket());
            if (packet.getEntity() instanceof Player && Knockback.inTest.contains(packet.getEntity().getName()) && !packet.getEntity().getName().equals(e.getPlayer().getName())) e.setCancelled(true);
        }
    }
}
