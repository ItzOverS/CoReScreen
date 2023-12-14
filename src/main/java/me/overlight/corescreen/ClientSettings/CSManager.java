package me.overlight.corescreen.ClientSettings;

import me.overlight.corescreen.ClientSettings.Modules.ChatVisibility;
import me.overlight.corescreen.ClientSettings.Modules.ClientVersion;
import me.overlight.corescreen.ClientSettings.Modules.Locale;
import me.overlight.corescreen.ClientSettings.Modules.RenderDistance;
import me.overlight.corescreen.Test.TestCheck;
import me.overlight.corescreen.Test.Tests.Knockback;
import me.overlight.corescreen.Test.Tests.Rotation;
import me.overlight.corescreen.Test.Tests.Shuffle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CSManager {
    public final static List<CSModule> modules = new ArrayList<>();

    static {
        modules.addAll(Arrays.asList(new ClientVersion(), new Locale(), new RenderDistance(), new ChatVisibility()));
    }
}
