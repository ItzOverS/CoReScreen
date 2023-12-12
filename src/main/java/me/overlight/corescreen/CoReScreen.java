package me.overlight.corescreen;

import io.github.retrooper.packetevents.PacketEvents;
import me.overlight.corescreen.Freeze.Cache.CacheManager;
import me.overlight.corescreen.Freeze.FreezeManager;
import me.overlight.corescreen.Freeze.Listeners;
import me.overlight.corescreen.Freeze.Warps.Listener;
import me.overlight.corescreen.Freeze.Warps.WarpManager;
import me.overlight.corescreen.Profiler.ProfileHandler;
import me.overlight.corescreen.Profiler.Profiles.NmsHandler;
import me.overlight.corescreen.Vanish.BackwardServerMessenger;
import me.overlight.corescreen.Vanish.PacketHandler;
import me.overlight.powerlib.PowerLib;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.function.Predicate;

public final class CoReScreen extends JavaPlugin {

    private static CoReScreen instance;

    public CoReScreen() {
        instance = this;
    }

    public static CoReScreen getInstance() {
        return instance;
    }

    public static Player getPlayer(String name) {
        try {
            return Bukkit.getOnlinePlayers().stream().filter(r -> r.getName().equals(name)).findFirst().get();
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public void onLoad() {
        PacketEvents.create(this);
        PacketEvents.get().getSettings()
                .checkForUpdates(false)
                .bStats(false).compatInjector(true)
                .lock();
        PacketEvents.get().load();
    }

    private Placeholders placeholder = null;

    @Override
    public void onEnable() {
        Commands.prefix = translate("messages.prefix");
        PowerLib.create(this);
        PacketEvents.get().init();
        { // Vanish System
            Bukkit.getScheduler().runTask(this, () -> PacketEvents.get().getEventManager().registerListener(new PacketHandler()));
            getServer().getPluginCommand("vanish").setExecutor(new Commands.Vanish());
            getServer().getPluginManager().registerEvents(new me.overlight.corescreen.Vanish.Listeners(), this);
        }
        { // Freeze System
            Bukkit.getScheduler().runTask(this, () -> PacketEvents.get().getEventManager().registerListener(new FreezeManager.Handler()));
            Bukkit.getScheduler().runTask(this, () -> PacketEvents.get().getEventManager().registerListener(new me.overlight.corescreen.Freeze.PacketHandler()));
            if(CacheManager.isEnabled) Bukkit.getScheduler().runTask(this, () -> PacketEvents.get().getEventManager().registerListener(new me.overlight.corescreen.Freeze.Cache.Listener()));
            getServer().getPluginManager().registerEvents(new Listeners(), this);
            if(WarpManager.isEnabled && WarpManager.protectWarp) getServer().getPluginManager().registerEvents(new Listener(), this);
            getServer().getPluginCommand("freeze").setExecutor(new Commands.Freeze());
        }
        { // Profiler System
            Bukkit.getScheduler().runTaskTimer(this, new ProfileHandler(), 10, getConfig().getInt("settings.profiler.profiler-update-delay"));
            getServer().getPluginCommand("profiler").setExecutor(new Commands.Profiler());
            getServer().getPluginCommand("profiler").setTabCompleter(new Commands.Profiler.TabComplete());
            NmsHandler.loadCustomSettings();
        }
        { // Tests System
            Bukkit.getScheduler().runTask(this, () -> PacketEvents.get().getEventManager().registerListener(new me.overlight.corescreen.Test.PacketHandler()));
            getServer().getPluginCommand("test").setExecutor(new Commands.Test());
            getServer().getPluginCommand("test").setTabCompleter(new Commands.Test.TabComplete());
        }
        saveDefaultConfig();

        if (isClassLoaded("me.clip.placeholderapi.PlaceholderAPI")) {
            placeholder = new Placeholders();
            placeholder.register();
        }

        BackwardServerMessenger.init();
    }

    @Override
    public void onDisable() {
        PowerLib.terminate();
        PacketEvents.get().terminate();
        BackwardServerMessenger.stop();
        if (placeholder != null && placeholder.isRegistered()) placeholder.unregister();
        Bukkit.getOnlinePlayers().forEach(p -> p.setWalkSpeed(.2f));
    }

    public static String translate(String path) {
        try {
            return ChatColor.translateAlternateColorCodes('&', CoReScreen.getInstance().getConfig().getString(path).replace("%prefix%", Commands.prefix == null ? "&e&lCoRe&cVanish &6»" : Commands.prefix));
        } catch (Exception ex) {
            return path;
        }
    }

    public static boolean isClassLoaded(String path) {
        try {
            Class.forName(path.trim());
        } catch (ClassNotFoundException e) {
            return false;
        }
        return true;
    }
}
