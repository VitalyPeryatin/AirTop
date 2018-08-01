package com.example.infinity.airtop.service.client;


import android.util.Log;

import com.example.infinity.airtop.service.client.readData.DataReader;
import com.example.infinity.airtop.service.client.writeData.DataWriter;
import com.example.infinity.airtop.App;

import java.io.IOException;
import java.net.Socket;

/**
 * Client of backend part. It connects to server and tune threads for reading and writing data
 * @author infinity_coder
 * @version 1.0.0
 */
public class BackendClient extends Thread{

    private Socket socket;
    private DataWriter writer;
    private DataReader reader;
    private static final String HOST = "192.168.1.65";
    private static final int PORT = 9090;
    private boolean quit = true; // Check when programme need to close connection

    // Client connects to server in a new thread, because working with internet network possible
    // only in a separate thread
    public BackendClient() {
        Log.d("mLog", "Подключение клиента");
        writer = new DataWriter(this);
        reader = new DataReader(this);
        writer.start();
        reader.start();
    }

    @Override
    public void run() {
        super.run();
        try {
            while (true) {
                while(!isQuit())
                    Thread.yield();
                // Connecting to server with host="192.168.1.81" and port=9090
                socket = new Socket(HOST, PORT);
                writer.setSocket(socket);
                reader.setSocket(socket);
                App.getInstance().verifyUser();
                quit = false;
            }

        } catch (Exception e) {
            quit = true;
            Log.d("mLog","Вылетело исключение");
            e.printStackTrace();
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


    /**
     * In case you need to close connections manually (DON'T REMOVE!)
     */
    private void closeConnection() {
        try {
            quit = true;
            reader.closeConnection();
            writer.closeConnection();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Charset for receiving and sending data in "DataReader" and "DataWriter"
     */
    public String getCharsetName(){
        return "windows-1251";
    }

    public void reloadServer(){
        closeConnection();
        quit = true; // TODO Возможно эту строку надо будет удалить
    }


}

