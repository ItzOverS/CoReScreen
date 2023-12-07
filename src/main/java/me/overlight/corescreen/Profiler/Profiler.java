package me.overlight.corescreen.Profiler;

import java.util.List;

public class Profiler {
    private final String staff, who;
    public List<ProfileOption> options;

    public Profiler(String staff, String who, List<ProfileOption> options) {
        this.staff = staff;
        this.who = who;
        this.options = options;
    }

    public String getStaff() {
        return staff;
    }

    public String getWho() {
        return who;
    }

    public enum ProfileOption {
        cps, movementspeed, ping, sneaking, sprinting, blocking, vrot, hrot, reach, fallspeed
    }
}
