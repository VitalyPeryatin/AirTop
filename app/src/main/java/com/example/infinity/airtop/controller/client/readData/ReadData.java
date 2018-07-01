package com.example.infinity.airtop.controller.client.readData;

import com.example.infinity.airtop.controller.client.Client;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.LinkedBlockingQueue;

public class ReadData extends Thread{

    private Client client;
    private DataInputStream din;
    private LinkedBlockingQueue<String> outMsgQueue;

    public ReadData(Client client, Socket socket, LinkedBlockingQueue<String> outMsgQueue) throws IOException {
        this.client = client;
        this.outMsgQueue = outMsgQueue;
        din = new DataInputStream(socket.getInputStream());
    }

    @Override
    public void run() {
        super.run();
        while(!client.isQuit()) {
            try {
                String text = din.readUTF();
                outMsgQueue.add(text);
            }
            catch (SocketException ignored){}
            catch (IOException e) {e.printStackTrace();}
        }
        closeConnection();
    }

    private synchronized void readData() throws IOException {

    }

    private synchronized void closeConnection(){
        try {
            din.close();
        } catch (IOException ignored) {}
    }
}
