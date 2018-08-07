package com.example.infinity.airtop.service.client;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;


/**
 * Class for writing data and sending to server
 * @author infinity_coder
 * @version 1.0.2
 */
public class DataWriter extends Thread{

    private ServerConnection serverConnection;
    private DataOutputStream outputStream;
    private LinkedBlockingQueue<String> msgQueue = new LinkedBlockingQueue<>();

    DataWriter(ServerConnection serverConnection) {
        this.serverConnection = serverConnection;
    }

    public void connectToSocket(Socket socket){
        try {
            outputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) { e.printStackTrace(); }
    }

    // Adds a message to the queue for following sending
    public synchronized void sendMessage(String json) {
        msgQueue.add(json);
    }

    @Override
    public void run() {
        super.run();
        while(true) {
            try {
                while (serverConnection.isQuit()) // waiting for connection to server
                    Thread.yield();
                while (msgQueue.size() == 0) // waiting for message queue to be added
                    Thread.yield();
                sendJsonToServer(msgQueue.poll());
                outputStream.flush();
            } catch (Exception e) { // after something error on the client is reconnect to server
                serverConnection.reconnectToServer();
            }
        }
    }

    private synchronized void sendJsonToServer(String jsonStr) throws IOException {
        byte[] bytes = jsonStr.getBytes(serverConnection.getCharsetName());
        outputStream.write(bytes, 0, bytes.length);
    }

    public void closeConnection() throws IOException {
        outputStream.close();
    }
}
