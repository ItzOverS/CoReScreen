package me.overlight.corescreen.Vanish;

import io.github.retrooper.packetevents.event.PacketListenerAbstract;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.event.impl.PacketPlaySendEvent;
import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.play.in.chat.WrappedPacketInChat;
import io.github.retrooper.packetevents.packetwrappers.play.in.useentity.WrappedPacketInUseEntity;
import io.github.retrooper.packetevents.packetwrappers.play.out.blockbreakanimation.WrappedPacketOutBlockBreakAnimation;
import io.github.retrooper.packetevents.packetwrappers.play.out.entity.WrappedPacketOutEntity;
import io.github.retrooper.packetevents.packetwrappers.play.out.entityeffect.WrappedPacketOutEntityEffect;
import io.github.retrooper.packetevents.packetwrappers.play.out.entityequipment.WrappedPacketOutEntityEquipment;
import io.github.retrooper.packetevents.packetwrappers.play.out.entityheadrotation.WrappedPacketOutEntityHeadRotation;
import io.github.retrooper.packetevents.packetwrappers.play.out.entitymetadata.WrappedPacketOutEntityMetadata;
import io.github.retrooper.packetevents.packetwrappers.play.out.entitystatus.WrappedPacketOutEntityStatus;
import io.github.retrooper.packetevents.packetwrappers.play.out.entityteleport.WrappedPacketOutEntityTeleport;
import io.github.retrooper.packetevents.packetwrappers.play.out.entityvelocity.WrappedPacketOutEntityVelocity;
import io.github.retrooper.packetevents.packetwrappers.play.out.namedentityspawn.WrappedPacketOutNamedEntitySpawn;
import io.github.retrooper.packetevents.packetwrappers.play.out.removeentityeffect.WrappedPacketOutRemoveEntityEffect;
import io.github.retrooper.packetevents.packetwrappers.play.out.tabcomplete.WrappedPacketOutTabComplete;
import me.overlight.corescreen.CoReScreen;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PacketHandler extends PacketListenerAbstract {
    public static final String see_other_permission = "corescreen.vanish.spect";

    @Override
    public void onPacketPlaySend(PacketPlaySendEvent e) {/*
        if (e.getPacketId() == PacketType.Play.Server.ANIMATION) {
            try {
                Class<?> cls = e.getNMSPacket().getRawNMSPacket().getClass();
                Field field_id = cls.getDeclaredField("a");
                field_id.setAccessible(true);
                int id = field_id.getInt(cls);
                field_id.setAccessible(false);
                List<Player> players = VanishManager.vanishes.stream().filter(r -> CoReScreen.getPlayer(r) != null).map(CoReScreen::getPlayer).collect(Collectors.toList());
                if(!players.stream().map(Player::getEntityId).collect(Collectors.toList()).contains(id)) return;
                if(!VanishManager.isVanish(players.stream().filter(f -> f.getEntityId() == id).findFirst().get())) return;
                if(!e.getPlayer().hasPermission(see_other_permission)) e.setCancelled(true);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else */if (e.getPacketId() == PacketType.Play.Server.ENTITY &&
                new WrappedPacketOutEntity(e.getNMSPacket()).getEntity() instanceof Player && !e.getPlayer().hasPermission(see_other_permission) &&
                VanishManager.isVanish((Player) new WrappedPacketOutEntity(e.getNMSPacket()).getEntity())) e.setCancelled(true);
        else if (e.getPacketId() == PacketType.Play.Server.REMOVE_ENTITY_EFFECT &&
                new WrappedPacketOutRemoveEntityEffect(e.getNMSPacket()).getEntity() instanceof Player && !e.getPlayer().hasPermission(see_other_permission) &&
                VanishManager.isVanish((Player) new WrappedPacketOutRemoveEntityEffect(e.getNMSPacket()).getEntity())) e.setCancelled(true);
        else if (e.getPacketId() == PacketType.Play.Server.NAMED_ENTITY_SPAWN &&
                new WrappedPacketOutNamedEntitySpawn(e.getNMSPacket()).getEntity() instanceof Player && !e.getPlayer().hasPermission(see_other_permission) &&
                VanishManager.isVanish((Player) new WrappedPacketOutNamedEntitySpawn(e.getNMSPacket()).getEntity())) e.setCancelled(true);
        else if (e.getPacketId() == PacketType.Play.Server.ENTITY_LOOK &&
                new WrappedPacketOutEntity.WrappedPacketOutEntityLook(e.getNMSPacket()).getEntity() instanceof Player && !e.getPlayer().hasPermission(see_other_permission) &&
                VanishManager.isVanish((Player) new WrappedPacketOutEntity.WrappedPacketOutEntityLook(e.getNMSPacket()).getEntity())) e.setCancelled(true);
        else if (e.getPacketId() == PacketType.Play.Server.REL_ENTITY_MOVE &&
                new WrappedPacketOutEntity.WrappedPacketOutRelEntityMove(e.getNMSPacket()).getEntity() instanceof Player && !e.getPlayer().hasPermission(see_other_permission) &&
                VanishManager.isVanish((Player) new WrappedPacketOutEntity.WrappedPacketOutRelEntityMove(e.getNMSPacket()).getEntity())) e.setCancelled(true);
        else if (e.getPacketId() == PacketType.Play.Server.REL_ENTITY_MOVE_LOOK &&
                new WrappedPacketOutEntity.WrappedPacketOutRelEntityMoveLook(e.getNMSPacket()).getEntity() instanceof Player && !e.getPlayer().hasPermission(see_other_permission) &&
                VanishManager.isVanish((Player) new WrappedPacketOutEntity.WrappedPacketOutRelEntityMoveLook(e.getNMSPacket()).getEntity())) e.setCancelled(true);
        else if (e.getPacketId() == PacketType.Play.Server.ENTITY_STATUS &&
                new WrappedPacketOutEntityStatus(e.getNMSPacket()).getEntity() instanceof Player && !e.getPlayer().hasPermission(see_other_permission) &&
                VanishManager.isVanish((Player) new WrappedPacketOutEntityStatus(e.getNMSPacket()).getEntity())) e.setCancelled(true);
        else if (e.getPacketId() == PacketType.Play.Server.ENTITY_METADATA &&
                new WrappedPacketOutEntityMetadata(e.getNMSPacket()).getEntity() instanceof Player && !e.getPlayer().hasPermission(see_other_permission) &&
                VanishManager.isVanish((Player) new WrappedPacketOutEntityMetadata(e.getNMSPacket()).getEntity())) e.setCancelled(true);
        else if (e.getPacketId() == PacketType.Play.Server.ENTITY_EQUIPMENT &&
                new WrappedPacketOutEntityEquipment(e.getNMSPacket()).getEntity() instanceof Player && !e.getPlayer().hasPermission(see_other_permission) &&
                VanishManager.isVanish((Player) new WrappedPacketOutEntityEquipment(e.getNMSPacket()).getEntity())) e.setCancelled(true);
        else if (e.getPacketId() == PacketType.Play.Server.ENTITY_HEAD_ROTATION &&
                new WrappedPacketOutEntityHeadRotation(e.getNMSPacket()).getEntity() instanceof Player && !e.getPlayer().hasPermission(see_other_permission) &&
                VanishManager.isVanish((Player) new WrappedPacketOutEntityHeadRotation(e.getNMSPacket()).getEntity())) e.setCancelled(true);
        else if (e.getPacketId() == PacketType.Play.Server.ENTITY_EFFECT &&
                new WrappedPacketOutEntityEffect(e.getNMSPacket()).getEntity() instanceof Player && !e.getPlayer().hasPermission(see_other_permission) &&
                VanishManager.isVanish((Player) new WrappedPacketOutEntityEffect(e.getNMSPacket()).getEntity())) e.setCancelled(true);
        else if (e.getPacketId() == PacketType.Play.Server.ENTITY_VELOCITY &&
                new WrappedPacketOutEntityVelocity(e.getNMSPacket()).getEntity() instanceof Player && !e.getPlayer().hasPermission(see_other_permission) &&
                VanishManager.isVanish((Player) new WrappedPacketOutEntityVelocity(e.getNMSPacket()).getEntity())) e.setCancelled(true);
        else if (e.getPacketId() == PacketType.Play.Server.ENTITY_TELEPORT &&
                new WrappedPacketOutEntityTeleport(e.getNMSPacket()).getEntity() instanceof Player && !e.getPlayer().hasPermission(see_other_permission) &&
                VanishManager.isVanish((Player) new WrappedPacketOutEntityTeleport(e.getNMSPacket()).getEntity())) e.setCancelled(true);
        else if (e.getPacketId() == PacketType.Play.Server.TAB_COMPLETE && !e.getPlayer().hasPermission(see_other_permission)) {
            WrappedPacketOutTabComplete packet = new WrappedPacketOutTabComplete(e.getNMSPacket());
            List<String> args = new ArrayList<>(Arrays.asList(packet.getMatches()));
            args.removeIf(VanishManager.vanishes::contains);
            String[] output = new String[args.size()];
            for (int i = 0; i < args.size(); i++) output[i] = args.get(i);
            packet.setMatches(output);
        } else if (e.getPacketId() == PacketType.Play.Server.BLOCK_BREAK_ANIMATION && new WrappedPacketOutBlockBreakAnimation(e.getNMSPacket()).getEntity() instanceof Player &&
                VanishManager.isVanish((Player) new WrappedPacketOutBlockBreakAnimation(e.getNMSPacket()).getEntity())) e.setCancelled(true);
    }

    private final boolean isChatMentionBlocked = CoReScreen.getInstance().getConfig().getBoolean("messages.vanish.game.chat-mention.enabled", true);

    @Override
    public void onPacketPlayReceive(PacketPlayReceiveEvent e) {
        if (e.getPacketId() == PacketType.Play.Client.USE_ENTITY) {
            WrappedPacketInUseEntity packet = new WrappedPacketInUseEntity(e.getNMSPacket());
            if (packet.getEntity() == null) return;
            if (packet.getAction() != WrappedPacketInUseEntity.EntityUseAction.ATTACK) return;
            if (VanishManager.isVanish(e.getPlayer()) && !e.getPlayer().hasPermission("corescreen.vanish.permission.damageother")) e.setCancelled(true);
            if (packet.getEntity() instanceof Player && VanishManager.isVanish((Player) packet.getEntity()) && !packet.getEntity().hasPermission("corescreen.vanish.permission.damageself")) e.setCancelled(true);
        } else if (isChatMentionBlocked && e.getPacketId() == PacketType.Play.Client.CHAT
                && e.getPlayer().hasPermission(see_other_permission)) {
            String text = new WrappedPacketInChat(e.getNMSPacket()).getMessage();
            if (text.startsWith("/")) return;
            if (VanishManager.vanishes.stream().anyMatch(text::contains)) {
                e.setCancelled(true);
                e.getPlayer().sendMessage(CoReScreen.translate("messages.vanish.game.chat-mention.block-message").replace("%player%", VanishManager.vanishes.stream().filter(text::contains).collect(Collectors.joining(", "))));
            }
        }
    }
}
