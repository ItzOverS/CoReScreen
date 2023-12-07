package me.overlight.corescreen.Vanish.Land;

import me.overlight.corescreen.CoReScreen;
import org.bukkit.scheduler.BukkitTask;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ReceiverModule {

    private final CoReScreen plugin = CoReScreen.getInstance();

    private BukkitTask task;
    private boolean taskCancelled = false;

    private final List<ReceiverListener> listeners = new ArrayList<>();

    private final int port;

    private ServerSocket serverSocket;

    ReceiverModule(int port) {
        this.port = port;
        server();
    }

    private void server() {
        task = plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                serverSocket = new ServerSocket(port);
                Socket socket;
                InputStream inputStream;
                ObjectInputStream objectInputStream;
                DataPacket dataPacket;
                while (serverSocket != null && !serverSocket.isClosed()) {
                    try {
                        socket = serverSocket.accept();
                        inputStream = socket.getInputStream();
                        objectInputStream = new ObjectInputStream(inputStream);
                        dataPacket = (DataPacket) objectInputStream.readObject();
                        if (!dataPacket.getLabel().equals("ping")) {
                            for (ReceiverListener listener : listeners) {
                                listener.receivePacketEvent(dataPacket);
                            }
                        } else {
                        }
                        socket.close();
                    } catch (ClassNotFoundException e) {
                        if (!taskCancelled) {
                            plugin.getLogger().severe("An error occurred while receiving the packet!");
                            plugin.getLogger().severe("Info: " + e.getLocalizedMessage());
                        }
                    }
                }
            } catch (IOException e) {
                if (!taskCancelled) {
                    plugin.getLogger().severe("Receiver hasn't been registered!");
                    plugin.getLogger().severe("Info: " + e.getLocalizedMessage());
                }
            }
        });
    }

    void close() {
        if (task != null && !taskCancelled) {
            task.cancel();
            taskCancelled = true;
        }
        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                plugin.getLogger().severe("Receiver hasn't been unregistered!");
                plugin.getLogger().severe("Info: " + e.getMessage());
            }
        }
    }

    public void addListener(ReceiverListener listener) {
        listeners.add(listener);
    }

    public void removeListener(ReceiverListener listener) {
        listeners.remove(listener);
    }
}
