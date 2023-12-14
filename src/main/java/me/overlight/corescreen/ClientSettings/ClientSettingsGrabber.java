package me.overlight.corescreen.ClientSettings;

import io.github.retrooper.packetevents.event.PacketListenerAbstract;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.play.in.settings.WrappedPacketInSettings;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.Set;

public class ClientSettingsGrabber extends PacketListenerAbstract {
    private final static HashMap<String, PlayerClientSettings> settings = new HashMap<>();
    @Override
    public void onPacketPlayReceive(PacketPlayReceiveEvent e) {
        if(e.getPacketId() == PacketType.Play.Client.SETTINGS){
            WrappedPacketInSettings packet = new WrappedPacketInSettings(e.getNMSPacket());
            settings.put(e.getPlayer().getName(), new PlayerClientSettings(packet.getViewDistance(), packet.getChatVisibility(), packet.getLocale(), packet.getDisplayedSkinParts()));
        }
    }

    public static PlayerClientSettings getSettings(Player player){
        return settings.getOrDefault(player.getName(), null);
    }

    public static class PlayerClientSettings {
        private final int renderDistance;
        private final WrappedPacketInSettings.ChatVisibility chatVisibility;
        private final String locale;
        private final Set<WrappedPacketInSettings.DisplayedSkinPart> displayedSkinPartSet;

        public PlayerClientSettings(int renderDistance, WrappedPacketInSettings.ChatVisibility chatVisibility, String locale, Set<WrappedPacketInSettings.DisplayedSkinPart> displayedSkinPartSet) {
            this.renderDistance = renderDistance;
            this.chatVisibility = chatVisibility;
            this.locale = locale;
            this.displayedSkinPartSet = displayedSkinPartSet;
        }

        public int getRenderDistance() {
            return renderDistance;
        }

        public WrappedPacketInSettings.ChatVisibility getChatVisibility() {
            return chatVisibility;
        }

        public String getLocale() {
            return locale;
        }

        public Set<WrappedPacketInSettings.DisplayedSkinPart> getDisplayedSkinPartSet() {
            return displayedSkinPartSet;
        }
    }
}
