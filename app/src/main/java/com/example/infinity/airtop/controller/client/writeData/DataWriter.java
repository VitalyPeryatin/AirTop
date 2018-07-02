package com.example.infinity.airtop.controller.client.writeData;

import com.example.infinity.airtop.controller.client.BackendClient;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.LinkedBlockingQueue;

public class WriteData extends Thread{

    private BackendClient backendClient;
    private DataOutputStream dout;
    private LinkedBlockingQueue<String> msgQueue = new LinkedBlockingQueue<>();

    public WriteData(BackendClient backendClient, Socket socket) throws IOException {
        this.backendClient = backendClient;
        dout = new DataOutputStream(socket.getOutputStream());
    }

    public LinkedBlockingQueue<String> getMsgQueue() {
        return msgQueue;
    }

    @Override
    public void run() {
        super.run();
        while(backendClient.isNotQuit()){
            try {
                while(msgQueue.size()==0)
                    Thread.yield();
                dout.writeUTF(msgQueue.poll());
                dout.flush();
            }
            catch (SocketException ignored) {}
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            dout.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
