package me.overlight.corescreen.Vanish.Land;

import me.overlight.corescreen.CoReScreen;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class SenderModule {
    private Socket socket;

    public void sendData(DataPacket data, ServerReceiver serverReceiver) {
        try {
            if (socket == null || socket.isClosed()) socket = new Socket(serverReceiver.getHost(), serverReceiver.getPort());
            OutputStream outputStream = socket.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(data);
            objectOutputStream.flush();
            objectOutputStream.close();
        } catch (IOException e) {
        }
    }

    public boolean ping(ServerReceiver serverReceiver) {
        try {
            if (socket == null || socket.isClosed()) socket = new Socket(serverReceiver.getHost(), serverReceiver.getPort());
            OutputStream outputStream = socket.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(new DataPacket(CoReScreen.getInstance(), "ping", null));
            objectOutputStream.flush();
            objectOutputStream.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    void close() {
        try {
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (IOException e) {
        }
    }
}

