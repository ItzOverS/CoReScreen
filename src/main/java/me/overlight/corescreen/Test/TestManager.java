package me.overlight.corescreen.Test;

import me.overlight.corescreen.Test.Tests.Knockback;
import me.overlight.corescreen.Test.Tests.Rotation;
import me.overlight.corescreen.Test.Tests.Shuffle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestManager {
    public final static List<TestCheck> tests = new ArrayList<>();

    static {
        tests.addAll(Arrays.asList(new Knockback(), new Rotation(), new Shuffle()));
    }
}
