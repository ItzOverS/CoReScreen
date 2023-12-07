package me.overlight.corescreen.Vanish.Land;

import me.overlight.corescreen.CoReScreen;

public class GlobalNetwork {

    private ReceiverModule receiver;
    private SenderModule sender;

    private void loadServerReceivers() {
        for (Integer port : CoReScreen.getInstance().getConfig().getIntegerList("settings.vanish.server-sync.other-server-ports")) {
            new ServerReceiver(
                    "Server_" + (Math.floor(Math.random() * 74846) * Math.floor(Math.random() * 99754)),
                    "127.0.0.1",
                    port
            ).register();
        }
    }

    public ReceiverModule getReceiver() {
        return receiver;
    }

    public SenderModule getSender() {
        return sender;
    }

    public void reload() {
        if (receiver != null) receiver.close();
        if (sender != null) sender.close();
        ServerReceiver.unregisterAll();
        receiver = new ReceiverModule(CoReScreen.getInstance().getConfig().getInt("settings.vanish.server-sync.port"));
        sender = new SenderModule();
        loadServerReceivers();
    }

    public void close() {
        receiver.close();
        sender.close();
        ServerReceiver.unregisterAll();
    }
}
