package com.example.infinity.airtop.controller.client;


import com.example.infinity.airtop.controller.client.readData.ReadData;
import com.example.infinity.airtop.controller.client.writeData.WriteData;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

public class Client {
    private Socket socket;

    public LinkedBlockingQueue<String> getInMsgQueue() {
        return inMsgQueue;
    }

    public LinkedBlockingQueue<String> getOutMsgQueue() {
        return outMsgQueue;
    }

    private LinkedBlockingQueue<String> inMsgQueue = new LinkedBlockingQueue<>();
    private LinkedBlockingQueue<String> outMsgQueue = new LinkedBlockingQueue<>();
    private boolean quit = false;


    public Client() throws IOException {
        socket = new Socket();
        socket.connect(new InetSocketAddress("192.168.1.81", 9090));
        System.out.println(socket.getLocalSocketAddress());
        try {
            WriteData writer = new WriteData(this, socket, inMsgQueue);
            ReadData reader = new ReadData(this, socket, outMsgQueue);
            writer.start();
            reader.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write(String text){
        inMsgQueue.add(text);
    }



    public boolean isQuit() {
        return quit;
    }


    public void close() throws IOException {

        quit = true;
        socket.close();
    }

}

