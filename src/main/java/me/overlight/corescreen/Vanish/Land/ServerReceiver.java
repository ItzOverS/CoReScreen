package me.overlight.corescreen.Vanish.Land;

import java.util.HashMap;
import java.util.Set;

public class ServerReceiver {
    private final static HashMap<String, ServerReceiver> serverslist = new HashMap<>();

    public static ServerReceiver getServerReceiver(String name) {
        return serverslist.get(name);
    }

    public static Set<String> getRegisteredServers() {
        return serverslist.keySet();
    }

    private final String name;
    private final String host;
    private final int port;

    ServerReceiver(String name, String host, int port) {
        this.name = name;
        this.host = host;
        this.port = port;
    }

    void register() {
        serverslist.put(name, this);
    }

    void unregister() {
        serverslist.remove(name);
    }

    public String getName() {
        return name;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }


    static void unregisterAll() {
        serverslist.clear();
    }
}
