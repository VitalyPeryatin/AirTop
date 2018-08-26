package com.infinity_coder.infinity.airtop.service.client;

import android.util.Log;

import com.infinity_coder.infinity.airtop.data.db.interactors.UserInteractor;
import com.infinity_coder.infinity.airtop.data.db.model.User;
import com.infinity_coder.infinity.airtop.data.network.request.VerifyUserRequest;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;


/**
 * Class for writing data and sending to server
 * @author infinity_coder
 * @version 1.0.4
 */
public class DataWriter extends Thread{

    private ServerConnection serverConnection;
    private DataOutputStream outputStream;
    private LinkedBlockingQueue<String> msgQueue = new LinkedBlockingQueue<>();
    private final Object lock = new Object();

    DataWriter(ServerConnection serverConnection) {
        this.serverConnection = serverConnection;
    }

    public void connectToSocket(Socket socket){
        try {
            outputStream = new DataOutputStream(socket.getOutputStream());
            verifyUserPhone();
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void verifyUserPhone(){
        UserInteractor interactor  = new UserInteractor();
        ArrayList<User> users = interactor.getAllUsers();
        if(users != null) {
            for (User user : users) {
                VerifyUserRequest request = new VerifyUserRequest(user.uuid);
                sendMessage(request.toJson());
            }
        }
    }

    // Adds a message to the queue for following sending
    public void sendMessage(String json) {
        synchronized (lock) {
            msgQueue.add(json);
            lock.notify();
        }
        Log.d("mLog", "json: " + json);
    }

    @SuppressWarnings("InfiniteLoopStatement")
    @Override
    public void run() {
        super.run();
        while (true) {
            try {
                while (serverConnection.isQuit()) // waiting for connection to server
                    Thread.yield();
                while (msgQueue.size() == 0)  // waiting for message queue to be added
                    synchronized (lock) {
                        lock.wait();
                    }
                sendJsonToServer(msgQueue.poll());
                Thread.sleep(100);
                outputStream.flush();
            } catch (Exception e) { // after something error on the client is reconnect to server
                serverConnection.reconnectToServer();
            }
        }
    }

    private synchronized void sendJsonToServer(String jsonStr) throws IOException {
        byte[] bytes = jsonStr.getBytes(serverConnection.getCharsetName());
        outputStream.write(bytes, 0, bytes.length);
    }

    public void closeConnection() throws IOException {
        outputStream.close();
    }
}
