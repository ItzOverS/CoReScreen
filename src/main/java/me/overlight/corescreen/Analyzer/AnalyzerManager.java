package me.overlight.corescreen.Analyzer;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.event.PacketListenerAbstract;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.event.impl.PacketPlaySendEvent;
import me.overlight.corescreen.Analyzer.Analyzers.Ping;
import me.overlight.corescreen.Analyzer.Analyzers.Reach;
import me.overlight.corescreen.CoReScreen;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AnalyzerManager {
    public final static List<AnalyzeModule> analyzers = new ArrayList<>();

    static {
        analyzers.addAll(Arrays.asList(new Ping(), new Reach()));

        Bukkit.getScheduler().runTaskLater(CoReScreen.getInstance(), () -> {
            PacketEvents.get().getEventManager().registerListener(new PacketListenerAbstract() {
                @Override
                public void onPacketPlayReceive(PacketPlayReceiveEvent event) {
                    analyzers.forEach(r -> r.packetEvent(event));
                }
                @Override
                public void onPacketPlaySend(PacketPlaySendEvent event) {
                    analyzers.forEach(r -> r.packetEvent(event));
                }
            });
        }, 5);
    }
}
