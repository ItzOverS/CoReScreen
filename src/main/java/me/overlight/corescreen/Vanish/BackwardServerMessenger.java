package me.overlight.corescreen.Vanish;

import me.overlight.corescreen.CoReScreen;
import me.overlight.corescreen.Vanish.Land.DataPacket;
import me.overlight.corescreen.Vanish.Land.GlobalNetwork;
import me.overlight.corescreen.Vanish.Land.ReceiverListener;
import me.overlight.corescreen.Vanish.Land.ServerReceiver;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.Arrays;

public class BackwardServerMessenger {
    private static boolean proxy = false, systemEnabled = false;

    private static final GlobalNetwork network = new GlobalNetwork();

    private static class Handler implements ReceiverListener {
        @Override
        public void receivePacketEvent(DataPacket data) {
            if (!systemEnabled) return;
            if (!data.getLabel().equals("vanish-update")) return;
            String[] args = Arrays.stream(String.valueOf(data.getData()).split(",")).map(String::trim).toArray(String[]::new);
            if (args[0].equals("enable")) VanishManager.vanishOffline(args[1]);
            if (args[0].equals("disable")) VanishManager.unVanishOffline(args[1]);
            Bukkit.getConsoleSender().sendMessage("[CoReScreen] Vanish updated for " + args[1] + " to " + args[0]);
        }
    }

    public static void sendData(String... data) {
        Bukkit.getScheduler().runTaskAsynchronously(CoReScreen.getInstance(), () -> {
            if (!proxy || !systemEnabled) return;
            DataPacket d = new DataPacket(CoReScreen.getInstance(), "vanish-update", String.join(",", data));
            for (String name : ServerReceiver.getRegisteredServers()) network.getSender().sendData(d, ServerReceiver.getServerReceiver(name));
        });
    }

    public static void init() {
        systemEnabled = CoReScreen.getInstance().getConfig().getBoolean("settings.vanish.server-sync.enabled");
        if (!systemEnabled) return;
        if (YamlConfiguration.loadConfiguration(new File("spigot.yml")).getBoolean("settings.bungeecord")
                || YamlConfiguration.loadConfiguration(new File("paper.yml")).getBoolean("settings.velocity-support.enabled")) {
            proxy = true;
            network.reload();
            network.getReceiver().addListener(new Handler());
        }
    }

    public static void stop() {
        if (!systemEnabled) return;
        network.close();
    }
}
