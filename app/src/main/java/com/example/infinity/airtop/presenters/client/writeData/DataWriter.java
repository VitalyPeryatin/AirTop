package com.example.infinity.airtop.controller.client.writeData;

import com.example.infinity.airtop.controller.client.BackendClient;
import com.example.infinity.airtop.model.Message;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

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
    private LinkedBlockingQueue<Message> msgQueue = new LinkedBlockingQueue<>();


    public DataWriter(BackendClient backendClient, Socket socket) throws IOException {
        this.backendClient = backendClient;
        outputStream = new DataOutputStream(socket.getOutputStream());
    }

    public void sendMessage(Message message) {
        msgQueue.add(message);
    }

    @Override
    public void run() {
        super.run();
        try {
            while (backendClient.isNotQuit()) { // forever writes messages at "endless" loop
                while (msgQueue.size() == 0) // waiting for message queue to be added
                    Thread.yield();
                String jsonMessage = transformToJson(msgQueue.poll());
                sendJson(jsonMessage);
                outputStream.flush();
            }

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

    private String transformToJson(@NotNull Message message){
        msgQueue.poll();
        if(message.getText() != null)
            new TextEncoder().encode(message);
        if(message.getImage() != null)
            new ImageEncoder().encode(message);
        Gson gson = new Gson();
        String jsonMessage = gson.toJson(message);
        jsonMessage = jsonMessage.length() + "@" + jsonMessage;

        return jsonMessage;
    }


    private void sendJson(String jsonStr) throws IOException {
        byte[] bytes = jsonStr.getBytes(backendClient.getCharsetName());
        outputStream.write(bytes, 0, bytes.length);
    }


}
