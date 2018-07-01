package com.example.infinity.airtop.controller.client.writeData;

import com.example.infinity.airtop.controller.client.Client;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;
import java.util.concurrent.LinkedBlockingQueue;

public class WriteData extends Thread{

    private Client client;
    private DataOutputStream dout;
    private LinkedBlockingQueue<String> inMsgQueue;

    public WriteData(Client client, Socket socket, LinkedBlockingQueue<String> inMsgQueue) throws IOException {
        this.client = client;
        this.inMsgQueue = inMsgQueue;
        dout = new DataOutputStream(socket.getOutputStream());
    }

    @Override
    public void run() {
        super.run();
        while(!client.isQuit()){
            try {
                String str = new Scanner(System.in).nextLine(); // Эта строка будет удалена
                client.write(str);                              // Эти строки будут перенесены в AsyncTask
                dout.writeUTF(str);
                inMsgQueue.poll();
                dout.flush();
            }
            catch (SocketException ignored) {

            }
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
