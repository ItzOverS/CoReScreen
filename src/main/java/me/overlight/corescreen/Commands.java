package me.overlight.corescreen;

import me.overlight.corescreen.Freeze.Cache.CacheManager;
import me.overlight.corescreen.Freeze.FreezeManager;
import me.overlight.corescreen.Freeze.Warps.WarpManager;
import me.overlight.corescreen.Profiler.ProfilerManager;
import me.overlight.corescreen.Profiler.Profiles.NmsHandler;
import me.overlight.corescreen.Profiler.ProfilingSystem;
import me.overlight.corescreen.Test.TestCheck;
import me.overlight.corescreen.Test.TestManager;
import me.overlight.corescreen.Vanish.VanishManager;
import me.overlight.corescreen.api.Freeze.PlayerFreezeEvent;
import me.overlight.corescreen.api.Freeze.PlayerUnfreezeEvent;
import me.overlight.powerlib.Chat.Text.impl.PlayerChatMessage;
import me.overlight.powerlib.Chat.Text.impl.ext.ClickableCommand;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.stream.Collectors;

public class Commands {
    public static String prefix;

    public static class Vanish implements CommandExecutor {
        private final HashMap<String, Long> cooldown_vanish = new HashMap<>();
        private final HashMap<String, Long> cooldown_unvanish = new HashMap<>();
        private final int vanishCooldown = CoReScreen.getInstance().getConfig().getInt("settings.vanish.command-cooldown.vanish"),
                unvanishCooldown = CoReScreen.getInstance().getConfig().getInt("settings.vanish.command-cooldown.unvanish");
        @Override
        public boolean onCommand(CommandSender commandSender, org.bukkit.command.Command command, String alias, String[] args) {
            if (args.length == 0) {
                if (!commandSender.hasPermission("corescreen.vanish.self")) {
                    commandSender.sendMessage(CoReScreen.translate("messages.no-permission"));
                    return false;
                }
                if (!(commandSender instanceof Player)) {
                    commandSender.sendMessage(CoReScreen.translate("messages.only-players"));
                    return false;
                }
                if ((!VanishManager.isVanish((Player) commandSender) && System.currentTimeMillis() - cooldown_vanish.getOrDefault(commandSender.getName(), 0L) > vanishCooldown) ||
                        (VanishManager.isVanish((Player) commandSender) && System.currentTimeMillis() - cooldown_unvanish.getOrDefault(commandSender.getName(), 0L) > unvanishCooldown)) {
                    if(VanishManager.isVanish((Player) commandSender)) cooldown_unvanish.put(commandSender.getName(), System.currentTimeMillis());
                    else cooldown_vanish.put(commandSender.getName(), System.currentTimeMillis());
                    VanishManager.toggleVanish((Player) commandSender);
                    if (VanishManager.isVanish((Player) commandSender)) {
                        commandSender.sendMessage(CoReScreen.translate("messages.vanish.command.you-are-vanish"));
                        DiscordWebhook.createDef(CoReScreen.getInstance().getConfig().getString("discord.webhooks.on-vanish")).setContent("**" + commandSender.getName() + "** has vanished they self!").execute();
                    } else {
                        commandSender.sendMessage(CoReScreen.translate("messages.vanish.command.you-are-unvanish"));
                        DiscordWebhook.createDef(CoReScreen.getInstance().getConfig().getString("discord.webhooks.on-unvanish")).setContent("**" + commandSender.getName() + "** has un-vanished they self!").execute();
                    }
                } else {
                    commandSender.sendMessage(CoReScreen.translate("settings.vanish.command-cooldown.message"));
                }
            } else if (args.length == 1) {
                if (!commandSender.hasPermission("corescreen.vanish.other")) {
                    commandSender.sendMessage(CoReScreen.translate("messages.no-permission"));
                    return false;
                }
                List<Player> LWho = Bukkit.getOnlinePlayers().stream().filter(r -> r.getName().equals(args[0])).collect(Collectors.toList());
                if (LWho.isEmpty()) {
                    commandSender.sendMessage(CoReScreen.translate("messages.player-offline").replace("%who%", args[0]));
                    return false;
                }
                Player who = LWho.get(0);
                VanishManager.toggleVanish(who);
                if (VanishManager.isVanish(who)) {
                    commandSender.sendMessage(CoReScreen.translate("messages.vanish.command.other-are-vanish").replace("%who%", who.getName()));
                    DiscordWebhook.createDef(CoReScreen.getInstance().getConfig().getString("discord.webhooks.on-vanish")).setContent("**" + who.getName() + "** has vanished by **" + commandSender.getName() + "**!").execute();
                } else {
                    commandSender.sendMessage(CoReScreen.translate("messages.vanish.command.other-are-unvanish").replace("%who%", who.getName()));
                    DiscordWebhook.createDef(CoReScreen.getInstance().getConfig().getString("discord.webhooks.on-unvanish")).setContent("**" + who.getName() + "** has un-vanished by **" + commandSender.getName() + "**!").execute();
                }
            } else if (args.length == 2) {
                if (!commandSender.hasPermission("corescreen.vanish.other")) {
                    commandSender.sendMessage(CoReScreen.translate("messages.no-permission"));
                    return false;
                }
                boolean forceEnabled = args[1].equalsIgnoreCase("on");
                List<Player> LWho = Bukkit.getOnlinePlayers().stream().filter(r -> r.getName().equals(args[0])).collect(Collectors.toList());
                if (LWho.isEmpty()) {
                    commandSender.sendMessage(CoReScreen.translate("messages.player-offline").replace("%who%", args[0]));
                    return false;
                }
                Player who = LWho.get(0);
                if (forceEnabled) VanishManager.vanishPlayer(who);
                else VanishManager.unVanishPlayer(who);
                if (VanishManager.isVanish(who)) commandSender.sendMessage(CoReScreen.translate("messages.vanish.command.other-are-vanish").replace("%who%", who.getName()));
                else commandSender.sendMessage(CoReScreen.translate("messages.vanish.command.other-are-unvanish").replace("%who%", who.getName()));
            }
            return false;
        }

        public static class TabComplete implements TabCompleter {
            @Override
            public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
                if(args.length == 1 && commandSender.hasPermission("corescreen.vanish.other")){
                    return Bukkit.getOnlinePlayers().stream().map(Player::getName).map(String::valueOf).filter(r -> r.startsWith(args[args.length - 1])).collect(Collectors.toList());
                }
                return null;
            }
        }
    }

    public static class Profiler implements CommandExecutor {
        @Override
        public boolean onCommand(CommandSender commandSender, org.bukkit.command.Command command, String alias, String[] args) {
            if (args.length == 1 && args[0].equalsIgnoreCase("remove")) {
                if (!commandSender.hasPermission("corescreen.profiler.remove")) {
                    commandSender.sendMessage(CoReScreen.translate("messages.no-permission"));
                    return false;
                }
                if (!(commandSender instanceof Player)) {
                    commandSender.sendMessage(CoReScreen.translate("messages.only-players"));
                    return false;
                }
                ProfilerManager.removeProfiler((Player) commandSender);
            } else if (args.length == 2) {
                if (!commandSender.hasPermission("corescreen.profiler.append")) {
                    commandSender.sendMessage(CoReScreen.translate("messages.no-permission"));
                    return false;
                }
                if (!(commandSender instanceof Player)) {
                    commandSender.sendMessage(CoReScreen.translate("messages.only-players"));
                    return false;
                }
                List<Player> LWho = Bukkit.getOnlinePlayers().stream().filter(r -> r.getName().equals(args[0])).collect(Collectors.toList());
                if (LWho.isEmpty()) {
                    commandSender.sendMessage(CoReScreen.translate("messages.player-offline").replace("%who%", args[0]));
                    return false;
                }
                Player who = LWho.get(0);
                try {
                    ProfilingSystem profiler = ProfilerManager.profilingSystems.stream().filter(r -> r.getName().equalsIgnoreCase(args[1])).collect(Collectors.toList()).get(0);
                    ProfilerManager.addProfiler((Player) commandSender, who, profiler.getMode());
                    commandSender.sendMessage(CoReScreen.translate("messages.profiler.profiler-success-add").replace("%who%", who.getName()).replace("%profiler%", args[1]));
                } catch (Exception ex) {
                    if(NmsHandler.handlers.stream().anyMatch(r -> r.getName().equalsIgnoreCase(args[1]))){
                        ProfilerManager.addProfiler((Player) commandSender, who, NmsHandler.handlers.stream().filter(r -> r.getName().equalsIgnoreCase(args[1])).findFirst().get());
                    } else {
                        commandSender.sendMessage(CoReScreen.translate("messages.profiler.invalid-profiler-name").replace("%name%", args[1]));
                    }
                }
            }
            return false;
        }

        public static class TabComplete implements TabCompleter {
            @Override
            public List<String> onTabComplete(CommandSender commandSender, Command command, String alias, String[] args) {
                if (args.length == 1) {
                    List<String> out = new ArrayList<>();
                    if(commandSender.hasPermission("corescreen.profiler.append")) out.addAll(Bukkit.getOnlinePlayers().stream().map(Player::getName).map(String::valueOf).filter(r -> r.startsWith(args[args.length - 1])).collect(Collectors.toList()));
                    if(commandSender.hasPermission("corescreen.profiler.remove")) out.add("remove");
                    return out;
                };
                if (args.length == 2 && commandSender.hasPermission("corescreen.profiler.append")) {
                    List<String> out = new ArrayList<>(ProfilerManager.profilingSystems.stream().map(ProfilingSystem::getName).map(String::toLowerCase).filter(r -> r.startsWith(args[args.length - 1])).collect(Collectors.toList()));
                    out.addAll(NmsHandler.handlers.stream().map(NmsHandler.NmsWrapper::getName).collect(Collectors.toList()));
                    return out;
                };
                return null;
            }
        }
    }

    public static class Test implements CommandExecutor {
        @Override
        public boolean onCommand(CommandSender commandSender, org.bukkit.command.Command command, String alias, String[] args) {
            if (args.length == 2) {
                if (!commandSender.hasPermission("corescreen.test")) {
                    commandSender.sendMessage(CoReScreen.translate("messages.no-permission"));
                    return false;
                }
                if (!(commandSender instanceof Player)) {
                    commandSender.sendMessage(CoReScreen.translate("messages.only-players"));
                    return false;
                }
                List<Player> LWho = Bukkit.getOnlinePlayers().stream().filter(r -> r.getName().equals(args[0])).collect(Collectors.toList());
                if (LWho.isEmpty()) {
                    commandSender.sendMessage(CoReScreen.translate("messages.player-offline").replace("%who%", args[0]));
                    return false;
                }
                Player who = LWho.get(0);
                TestCheck test = null;
                try {
                    test = TestManager.tests.stream().filter(r -> Arrays.asList(r.getAlias()).contains(args[1])).collect(Collectors.toList()).get(0);
                } catch (Exception ex) {
                }
                if (test == null) {
                    commandSender.sendMessage(CoReScreen.translate("messages.test.test-not-found").replace("%test%", args[1]));
                    return false;
                }
                if (!commandSender.hasPermission("corescreen.test." + test.getName().toLowerCase())) {
                    commandSender.sendMessage(CoReScreen.translate("messages.no-permission"));
                    return false;
                }
                test.handle(who, (Player) commandSender);
            }
            return false;
        }

        public static class TabComplete implements TabCompleter {
            @Override
            public List<String> onTabComplete(CommandSender commandSender, Command command, String alias, String[] args) {
                if (args.length == 1) return Bukkit.getOnlinePlayers().stream().map(Player::getName).map(String::valueOf).filter(r -> r.startsWith(args[args.length - 1])).collect(Collectors.toList());
                if (args.length == 2) return TestManager.tests.stream().map(r -> r.getAlias()[0]).map(String::toLowerCase).filter(r -> r.startsWith(args[args.length - 1])).collect(Collectors.toList());
                return null;
            }
        }
    }

    public static class Freeze implements CommandExecutor {
        @Override
        public boolean onCommand(CommandSender commandSender, org.bukkit.command.Command command, String alias, String[] args) {
            if (args.length == 1) {
                if (!commandSender.hasPermission("corescreen.freeze")) {
                    commandSender.sendMessage(CoReScreen.translate("messages.no-permission"));
                    return false;
                }
                List<Player> LWho = Bukkit.getOnlinePlayers().stream().filter(r -> r.getName().equals(args[0])).collect(Collectors.toList());
                if (LWho.isEmpty()) {
                    commandSender.sendMessage(CoReScreen.translate("messages.player-offline").replace("%who%", args[0]));
                    return false;
                }
                Player who = LWho.get(0);
                if (who.getName().equals(commandSender.getName()) && (commandSender instanceof Player && !FreezeManager.isFrozen((Player) commandSender))) {
                    who.sendMessage(CoReScreen.translate("messages.freeze.command.freeze-self"));
                    return false;
                }
                FreezeManager.toggleFreeze(who);
                if (FreezeManager.isFrozen(who)) {
                    PlayerFreezeEvent event = new PlayerFreezeEvent(false, who, !(commandSender instanceof Player)? null: (Player) commandSender);
                    Bukkit.getServer().getPluginManager().callEvent(event);
                    if(event.isCancelled()) return false;

                    commandSender.sendMessage(CoReScreen.translate("messages.freeze.command.you-freeze-other").replace("%who%", args[0]));
                    Bukkit.getOnlinePlayers().stream().filter(p -> p.hasPermission("corescreen.freeze.alert")).filter(p -> !p.getName().equals(commandSender.getName())).forEach(p -> p.sendMessage(CoReScreen.translate("messages.freeze.command.freeze-other").replace("%other%", args[0]).replace("%who%", commandSender.getName())));
                    final List<Integer> ignored$alerts = CoReScreen.getInstance().getConfig().getIntegerList("settings.freeze.time-remaining-alert.ignore-alert"),
                            manual$alerts = CoReScreen.getInstance().getConfig().getIntegerList("settings.freeze.time-remaining-alert.alert-come-to");
                    final int auto$alerts = CoReScreen.getInstance().getConfig().getInt("settings.freeze.time-remaining-alert.alert-every");
                    if(WarpManager.isEnabled && !WarpManager.warpPlayerToEmpty(who)) commandSender.sendMessage(CoReScreen.translate("messages.freeze.command.warping-failed").replace("%other%", who.getName()));
                    new BukkitRunnable() {
                        private int counter = CoReScreen.getInstance().getConfig().getInt("settings.freeze.time-remaining-alert.full");

                        @Override
                        public void run() {
                            if (!who.isOnline()) {
                                cancel();
                                return;
                            }
                            if (!FreezeManager.isFrozen(who)) {
                                cancel();
                                return;
                            }
                            if (counter < 0) {
                                who.sendMessage(CoReScreen.translate("settings.freeze.time-remaining-alert.times-up"));
                                Bukkit.getOnlinePlayers().stream().filter(p -> p.hasPermission("corescreen.freeze.alert")).forEach(p -> {
                                    p.sendMessage(CoReScreen.translate("settings.freeze.time-remaining-alert.times-up-staff").replace("%who%", who.getName()));
                                    List<BaseComponent> components = new ArrayList<>();
                                    for (String k : CoReScreen.getInstance().getConfig().getStringList("settings.freeze.time-remaining-alert.action-message")) {
                                        if (k.startsWith("%")) {
                                            ConfigurationSection section = CoReScreen.getInstance().getConfig().getConfigurationSection("settings.freeze.time-remaining-alert.actions." + k.replace("%", "").trim());
                                            if (section != null) {
                                                components.add(new PlayerChatMessage(ChatColor.translateAlternateColorCodes('&', section.getString("content")
                                                        .replace("%who%", who.getName())
                                                        .replace("%prefix%", Commands.prefix == null ? "&e&lCoRe&cVanish &6»" : Commands.prefix)))
                                                        .click(new ClickableCommand(section.getString("command").replace("%who%", who.getName())))
                                                        .hover(ChatColor.translateAlternateColorCodes('&', section.getString("hover").replace("%who%", who.getName()))).getComponent());
                                            }
                                        } else {
                                            components.add(new PlayerChatMessage(ChatColor.translateAlternateColorCodes('&', k)).getComponent());
                                        }
                                    }
                                    p.spigot().sendMessage(components.toArray(new BaseComponent[0]));
                                });
                                cancel();
                                return;
                            }
                            if (ignored$alerts.contains(counter)) {
                                counter--;
                                return;
                            }
                            if (counter % auto$alerts == 0 || manual$alerts.contains(counter)) {
                                String text = CoReScreen.translate("settings.freeze.time-remaining-alert.message");
                                if (counter % 60 == 0) text = text.replace("%time%", (counter / 60) + " minute" + (counter > 60 ? "s" : ""));
                                else if (counter > 60) text = text.replace("%time%", (counter / 60) + " minutes " + (counter % 60) + " second" + (counter % 60 == 1 ? "" : "s"));
                                else text = text.replace("%time%", (counter % 60) + " second" + (counter % 60 == 1 ? "" : "s"));
                                String text2 = CoReScreen.translate("settings.freeze.time-remaining-alert.message-staff").replace("%who%", who.getName());
                                if (!manual$alerts.contains(counter)) {
                                    if (counter % 60 == 0) text2 = text2.replace("%time%", (counter / 60) + " minute" + (counter > 60 ? "s" : ""));
                                    else if (counter > 60) text2 = text2.replace("%time%", (counter / 60) + " minutes " + (counter % 60) + " second" + (counter % 60 == 1 ? "" : "s"));
                                    else text2 = text2.replace("%time%", (counter % 60) + " second" + (counter % 60 == 1 ? "" : "s"));
                                }
                                who.sendMessage(text);
                                final String finalText = text2;
                                if (!manual$alerts.contains(counter)) Bukkit.getOnlinePlayers().stream().filter(p -> p.hasPermission("corescreen.freeze.alert")).forEach(p -> p.sendMessage(finalText));
                                CacheManager.cache(who, "[TIMER]" + text2);
                            }
                            counter--;
                        }
                    }.runTaskTimer(CoReScreen.getInstance(), 0, 20);
                    DiscordWebhook.createDef(CoReScreen.getInstance().getConfig().getString("discord.webhooks.on-freeze")).setContent("**" + who.getName() + "** has frozen by **" + commandSender.getName() + "**!").execute();
                } else {
                    PlayerUnfreezeEvent event = new PlayerUnfreezeEvent(false, who, !(commandSender instanceof Player)? null: (Player) commandSender);
                    Bukkit.getServer().getPluginManager().callEvent(event);
                    if(event.isCancelled()) return false;

                    commandSender.sendMessage(CoReScreen.translate("messages.freeze.command.you-unfreeze-other").replace("%who%", args[0]));
                    Bukkit.getOnlinePlayers().stream().filter(p -> p.hasPermission("corescreen.freeze.alert")).filter(p -> !p.getName().equals(commandSender.getName())).forEach(p -> p.sendMessage(CoReScreen.translate("messages.freeze.command.unfreeze-other").replace("%other%", args[0]).replace("%who%", commandSender.getName())));
                    DiscordWebhook.createDef(CoReScreen.getInstance().getConfig().getString("discord.webhooks.on-unfreeze")).setContent("**" + who.getName() + "** has un-frozen by **" + commandSender.getName() + "**!").execute();
                }
            } else if (args.length == 2 && args[1].equalsIgnoreCase("login")) {
                if (!commandSender.hasPermission("corescreen.freeze")) {
                    commandSender.sendMessage(CoReScreen.translate("messages.no-permission"));
                    return false;
                }
                if (args[0].equals(commandSender.getName())) {
                    commandSender.sendMessage(CoReScreen.translate("messages.freeze.command.freeze-self"));
                    return false;
                }
                if (Bukkit.getOnlinePlayers().stream().noneMatch(p -> p.getName().equalsIgnoreCase(args[0]))) {
                    if (FreezeManager.freezeWhenLogin.contains(args[0])) return false;
                    commandSender.sendMessage(CoReScreen.translate("messages.freeze.command.frozen-when-login").replace("%who%", args[0]));
                    FreezeManager.freezeWhenLogin.add(args[0]);
                } else {
                    FreezeManager.freezePlayer(Bukkit.getOnlinePlayers().stream().filter(p -> p.getName().equalsIgnoreCase(args[0])).collect(Collectors.toList()).get(0));
                    commandSender.sendMessage(CoReScreen.translate("messages.freeze.command.you-freeze-other").replace("%who%", args[0]));
                    Bukkit.getOnlinePlayers().stream().filter(p -> p.hasPermission("corescreen.freeze.alert")).filter(p -> !p.getName().equals(commandSender.getName())).forEach(p -> p.sendMessage(CoReScreen.translate("messages.freeze.command.freeze-other").replace("%other%", args[0]).replace("%who%", commandSender.getName())));
                }
            } else if (args.length == 2 && args[1].equalsIgnoreCase("off")) {
                if (!commandSender.hasPermission("corescreen.freeze")) {
                    commandSender.sendMessage(CoReScreen.translate("messages.no-permission"));
                    return false;
                }
                if (args[0].equals(commandSender.getName())) {
                    commandSender.sendMessage(CoReScreen.translate("messages.freeze.command.freeze-self"));
                    return false;
                }
                if (CoReScreen.getPlayer(args[0]) != null) {
                    PlayerUnfreezeEvent event = new PlayerUnfreezeEvent(false, CoReScreen.getPlayer(args[0]), !(commandSender instanceof Player)? null: (Player) commandSender);
                    Bukkit.getServer().getPluginManager().callEvent(event);
                    if(event.isCancelled()) return false;

                    FreezeManager.unfreezePlayer(Objects.requireNonNull(CoReScreen.getPlayer(args[0])));
                    commandSender.sendMessage(CoReScreen.translate("messages.freeze.command.you-unfreeze-other").replace("%who%", args[0]));
                    Bukkit.getOnlinePlayers().stream().filter(p -> p.hasPermission("corescreen.freeze.alert")).filter(p -> !p.getName().equals(commandSender.getName())).forEach(p -> p.sendMessage(CoReScreen.translate("messages.freeze.command.unfreeze-other").replace("%other%", args[0]).replace("%who%", commandSender.getName())));
                }
            }
            return false;
        }

        public static class TabComplete implements TabCompleter {
            @Override
            public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
                if(args.length == 1 && commandSender.hasPermission("corescreen.freeze")){
                    return Bukkit.getOnlinePlayers().stream().map(Player::getName).map(String::valueOf).filter(r -> r.startsWith(args[args.length - 1])).collect(Collectors.toList());
                }
                return null;
            }
        }
    }
}
