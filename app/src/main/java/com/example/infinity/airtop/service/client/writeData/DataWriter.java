package com.example.infinity.airtop.service.client.writeData;

import com.example.infinity.airtop.service.client.BackendClient;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;


/**
 * Class for writing data and sending on the server
 * @author infinity_coder
 * @version 1.0.0
 */
public class DataWriter extends Thread{

    private BackendClient backendClient; // object for controlling of closing connection
    private DataOutputStream outputStream;
    private LinkedBlockingQueue<String> msgQueue = new LinkedBlockingQueue<>();
    private Socket socket;

    public DataWriter(BackendClient backendClient) {
        this.backendClient = backendClient;
    }

    public void setSocket(Socket socket){
        this.socket = socket;
        updateOutputStream();
    }

    private void updateOutputStream(){
        try {
            outputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public synchronized void sendMessage(String json) {
        msgQueue.add(json);
    }

    @Override
    public void run() {
        super.run();
        while(true) {
            try {
                while (backendClient.isQuit())
                    Thread.yield();// forever writes messages at "endless" loop
                while (msgQueue.size() == 0) // waiting for message queue to be added
                    Thread.yield();
                sendJson(msgQueue.poll());
                outputStream.flush();

            } catch (Exception ignored) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                backendClient.reloadServer();
                // TODO Создать самоперезапускающийся сервер с последующей отправкой непосланных сообщений
            }
        }
    }




    private synchronized void sendJson(String jsonStr) throws IOException {
        byte[] bytes = jsonStr.getBytes(backendClient.getCharsetName());
        outputStream.write(bytes, 0, bytes.length);
    }

    public void closeConnection() throws IOException {
        outputStream.close();
    }


}
