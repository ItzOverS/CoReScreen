package me.overlight.corescreen.Analyzer;

import me.overlight.corescreen.Analyzer.Analyzers.Ping;
import me.overlight.corescreen.Test.TestCheck;
import me.overlight.corescreen.Test.Tests.Knockback;
import me.overlight.corescreen.Test.Tests.Rotation;
import me.overlight.corescreen.Test.Tests.Shuffle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AnalyzerManager {
    public final static List<AnalyzeModule> analyzers = new ArrayList<>();

    static {
        analyzers.addAll(Arrays.asList(new Ping()));
    }
}
