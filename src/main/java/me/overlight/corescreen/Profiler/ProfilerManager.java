package me.overlight.corescreen.Profiler;

import me.overlight.corescreen.CoReScreen;
import me.overlight.corescreen.Profiler.Profiles.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ProfilerManager {
    public final static List<Profiler> profilers = new ArrayList<>();
    public final static List<NmsHandler.NmsProfiler> profilersNms = new ArrayList<>();
    public final static List<ProfilingSystem> profilingSystems = new ArrayList<>();

    static {
        profilingSystems.addAll(Arrays.asList(new CPS(), new MovementSpeed(), new Ping(), new Sneaking(), new Sprinting(), new SwordBlocking(), new HorizontalRotationSpeed(),
                new VerticalRotationSpeed(), new Reach(), new FallSpeed()));
        profilingSystems.stream().filter(r -> r instanceof Listener).forEach(r -> CoReScreen.getInstance().getServer().getPluginManager().registerEvents((Listener) r, CoReScreen.getInstance()));
    }

    public static void addProfiler(Player staff, Player who, Profiler.ProfileOption... options) {
        profilers.removeIf(p -> p.getStaff().equals(staff.getName()) && !p.getWho().equals(who.getName()));
        if (profilers.stream().anyMatch(r -> r.getStaff().equals(staff.getName()) && r.getWho().equals(who.getName())))
            profilers.stream().filter(r -> r.getStaff().equals(staff.getName()) && r.getWho().equals(who.getName())).collect(Collectors.toList()).get(0).options.addAll(new ArrayList<>(Arrays.asList(options)));
        else profilers.add(new Profiler(staff.getName(), who.getName(), new ArrayList<>(Arrays.asList(options))));
    }

    public static void addProfiler(Player staff, Player who, NmsHandler.NmsWrapper... options) {
        profilersNms.removeIf(p -> p.getStaff().equals(staff.getName()) && !p.getWho().equals(who.getName()));
        if (profilersNms.stream().anyMatch(r -> r.getStaff().equals(staff.getName()) && r.getWho().equals(who.getName())))
            profilersNms.stream().filter(r -> r.getStaff().equals(staff.getName()) && r.getWho().equals(who.getName())).collect(Collectors.toList()).get(0).nmsWrapper.addAll(new ArrayList<>(Arrays.asList(options)));
        else profilersNms.add(new NmsHandler.NmsProfiler(staff.getName(), who.getName(), new ArrayList<>(Arrays.asList(options))));
    }

    public static void removeProfiler(Player staff) {
        profilers.removeIf(p -> p.getStaff().equals(staff.getName()));
        profilersNms.removeIf(p -> p.getStaff().equals(staff.getName()));
    }

    public static void removeProfiler(String staff) {
        profilers.removeIf(p -> p.getStaff().equals(staff));
        profilersNms.removeIf(p -> p.getStaff().equals(staff));
    }

    public static boolean isProfiling(String staff) {
        return profilers.stream().anyMatch(r -> r.getStaff().equals(staff)) || profilersNms.stream().anyMatch(r -> r.getStaff().equals(staff));
    }
}
