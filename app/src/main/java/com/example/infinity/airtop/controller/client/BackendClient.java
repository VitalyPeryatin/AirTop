package com.example.infinity.airtop.controller.client;


import android.content.Context;

import com.example.infinity.airtop.controller.client.readData.ReadData;
import com.example.infinity.airtop.controller.client.writeData.WriteData;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

public class Client{
    private Socket socket;
    private Context context;
    private WriteData writer;
    private ReadData reader;

    public LinkedBlockingQueue<String> getInMsgQueue() {
        return writer.getMsgQueue();
    }

    private boolean quit = false;


    public Client(Context context) {
        this.context = context;
        new Thread(new Runnable() {
            @Override
            public void run() {
                connectToServer();
            }
        }).start();

    }

    private void connectToServer(){
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress("192.168.1.81", 9090));
            writer = new WriteData(this, socket);
            reader = new ReadData(context, this, socket);
            writer.start();
            reader.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public boolean isQuit() {
        return quit;
    }


    public void close() {
        quit = true;
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

