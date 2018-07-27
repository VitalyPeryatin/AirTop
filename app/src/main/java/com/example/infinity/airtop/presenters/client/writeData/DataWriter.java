package com.example.infinity.airtop.presenters.client.writeData;

import android.util.Log;

import com.example.infinity.airtop.models.PhoneVerifier;
import com.example.infinity.airtop.models.RequestModel;
import com.example.infinity.airtop.models.SearchableUsers;
import com.example.infinity.airtop.models.User;
import com.example.infinity.airtop.presenters.client.BackendClient;
import com.example.infinity.airtop.models.Message;
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
    private LinkedBlockingQueue<RequestModel> msgQueue = new LinkedBlockingQueue<>();


    public DataWriter(BackendClient backendClient, Socket socket) throws IOException {
        this.backendClient = backendClient;
        outputStream = new DataOutputStream(socket.getOutputStream());
    }

    public synchronized void sendMessage(RequestModel requestModel) {
        msgQueue.add(requestModel);
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

    private String transformToJson(@NotNull RequestModel requestModel){
        //msgQueue.poll();
        Gson gson = new Gson();
        String jsonMessage = "";
        if(requestModel instanceof Message) {
            Message message = (Message) requestModel;
            if (message.getText() != null)
                new TextEncoder().encode(message);
            if (message.getImage() != null)
                new ImageEncoder().encode(message);
            jsonMessage = gson.toJson(message);
        }
        else if(requestModel instanceof User){
            User user = (User) requestModel;
            jsonMessage = gson.toJson(user);
        }
        else if(requestModel instanceof SearchableUsers){
            SearchableUsers searchableUsers = (SearchableUsers) requestModel;
            jsonMessage = gson.toJson(searchableUsers);
        }
        else if(requestModel instanceof PhoneVerifier){
            PhoneVerifier phoneVerifier = (PhoneVerifier) requestModel;
            jsonMessage = gson.toJson(phoneVerifier);
        }
        jsonMessage = jsonMessage.length() + "@" + jsonMessage;
        return jsonMessage;
    }


    private synchronized void sendJson(String jsonStr) throws IOException {
        byte[] bytes = jsonStr.getBytes(backendClient.getCharsetName());
        outputStream.write(bytes, 0, bytes.length);
    }


}
