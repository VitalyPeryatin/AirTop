package com.example.infinity.airtop.presenters.client;


import android.content.Context;
import android.util.Log;

import com.example.infinity.airtop.models.PhoneVerifier;
import com.example.infinity.airtop.models.RequestModel;
import com.example.infinity.airtop.presenters.client.readData.DataReader;
import com.example.infinity.airtop.presenters.client.writeData.DataWriter;
import com.example.infinity.airtop.models.Message;
import com.example.infinity.airtop.views.App;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Client of backend part. It connects to server and tune threads for reading and writing data
 * @author infinity_coder
 * @version 1.0.0
 */
public class BackendClient{

    private Socket socket;
    private DataWriter writer;
    private DataReader reader;
    private static final String HOST = "192.168.1.66";
    private static final int PORT = 9090;
    private boolean quit = true; // Check when programme need to close connection

    // Client connects to server in a new thread, because working with internet network possible
    // only in a separate thread
    public BackendClient() {
        Log.d("mLog", "Подключение клиента");
        new Thread(this::connectToServer).start();
    }

    // TODO Настроить корректную работу при отключёной сети или WiFi
    private void connectToServer(){
        try {
            while (!isNotQuit()) {
                // Connecting to server with host="192.168.1.81" and port=9090
                socket = new Socket(HOST, PORT);
                quit = false;
                writer = new DataWriter(this, socket);
                reader = new DataReader(this, socket);
                writer.start();
                reader.start();

            }

        } catch (Exception e) {
            quit = true;
            Log.d("mLog","Вылетело исключение");
            e.printStackTrace();
        }
        Log.d("mLog", "quit: " + quit);
    }



    /**
     * Check when programme need to close connection
     * @return "permission" to retain the server
     */
    public boolean isNotQuit() {
        return !quit;
    }

    /**
     * Pass message to DataWriter (class for sending processed messages)
     * @param requestModel request from user
     */
    public void sendRequest(RequestModel requestModel){
        while(writer == null)
            Thread.yield();
        writer.sendMessage(requestModel);
    }


    /**
     * In case you need to close connections manually (DON'T REMOVE!)
     */
    public void closeConnection() {
        try {
            socket.close();
            quit = true;
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
        //closeConnection();
        new Thread(this::connectToServer).start();
    }


}

