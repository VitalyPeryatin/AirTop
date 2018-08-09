package com.example.infinity.airtop.service.client;


import android.util.Log;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * Client of backend part. It connects to server and tune threads for reading and writing data
 * @author infinity_coder
 * @version 1.0.4
 */
public class ServerConnection extends Thread{

    private Socket socket;
    private SocketAddress address = new InetSocketAddress(HOST, PORT);
    private DataWriter writer;
    private DataReader reader;
    private static final String HOST = "192.168.1.65";
    private static final int PORT = 9090;
    private boolean quit = true; // Check when programme need to close connection
    private final Object lock = new Object();

    // Client connects to server in a new thread, because working with internet network possible
    // only in a separate thread
    public ServerConnection() {
        Log.d("mLog", "Подключение клиента... ");
        writer = new DataWriter(this);
        reader = new DataReader(this);
        writer.start();
        reader.start();
    }

    @SuppressWarnings("InfiniteLoopStatement")
    @Override
    public void run() {
        super.run();
        while (true) {
            try {
                while(!isQuit())
                    synchronized (lock) {
                        lock.wait();
                    }
                socket = new Socket();
                socket.connect(address, 1000); // Time for connecting to server
                writer.connectToSocket(socket);
                reader.connectToSocket(socket);
                quit = false;
                Log.d("mLog","Соединение с сервером найдено");
            } catch (Exception e) {
                quit = true;
                e.printStackTrace();
                Log.d("mLog","Подключение разорвано");
            }
        }
    }


    /**
     * Check when programme need to close connection
     * @return "permission" to retain the server
     */
    public boolean isQuit() {
        return quit;
    }

    /**
     * Pass message to DataWriter (class for sending processed messages)
     * @param json request from user
     */
    public void sendRequest(String json){
        while(writer == null)
            Thread.yield();
        writer.sendMessage(json);
    }


    // Safely closes an existing socket connection
    private void closeConnection() {
        try {
            quit = true;
            reader.closeConnection();
            writer.closeConnection();
            socket.close();
            synchronized (lock){
                lock.notify();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Charset name for sending and receiving data from the server
     */
    public String getCharsetName(){
        return "windows-1251";
    }

    /**
     * Automatically reconnect to server after closing the connection
     */
    public void reconnectToServer(){
        closeConnection();
    }
}

