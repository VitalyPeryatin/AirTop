package com.example.infinity.airtop.service.client.readData;

import android.util.Log;

import com.example.infinity.airtop.data.network.CheckingUsername;
import com.example.infinity.airtop.data.network.SearchableUsers;
import com.example.infinity.airtop.data.network.UserRequest;
import com.example.infinity.airtop.ui.searchUser.SearchUserPresenter;
import com.example.infinity.airtop.service.client.BackendClient;
import com.example.infinity.airtop.data.network.Message;
import com.example.infinity.airtop.App;
import com.example.infinity.airtop.ui.usernameUpdater.UsernameUpdaterPresenter;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;


/**
 * Class for reading the received data and publishing on the activity
 * @author infinity_coder
 * @version 1.0.0
 */
public class DataReader extends Thread{
    private BackendClient backendClient; // object for controlling of closing connection
    private DataInputStream inputStream;
    private Socket socket;
    private App.Listeners listeners;

    public DataReader(BackendClient backendClient) {
        this.backendClient = backendClient;
        listeners = App.getInstance().getListeners();
    }

    public void setSocket(Socket socket){
        this.socket = socket;
        updateInputStream();
    }

    private void updateInputStream(){
        try {
            inputStream = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        super.run();
        while(true) {
            try {
                while (backendClient.isQuit())
                    Thread.yield();
                try {
                    publishMessage(readData());
                } catch (SocketException se) {
                    backendClient.reloadServer();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *  Bitwise receives data from the server
     *  @return json-string for transformation to "Message" object and following publication
     */
    private String readData() throws IOException {
        StringBuilder n = new StringBuilder();
        byte[] b = new byte[1];
        while ((b[0] = inputStream.readByte()) != (byte)'@')
            n.append(new String(b, 0, 1));
        int bytesLen = Integer.parseInt(n.toString());
        byte[] bytes = new byte[bytesLen];
        inputStream.readFully(bytes);
        return new String(bytes, backendClient.getCharsetName());
    }


    /**
     * Converts the received string to the "Message" object and sends it to a publication at the Activity
     * @param jsonText json-string for transformation to "Message" object and following publication
     */
    private void publishMessage(String jsonText){
        Gson gson = new Gson();
        try {
            JSONObject jsonObject = new JSONObject(jsonText);
            Log.d("mLog", "Сообщение принято: " + jsonText);
            String type = jsonObject.getString("TYPE");
            if(type.equals("message")) {
                Message message = gson.fromJson(jsonText, Message.class);
                if (message.getText() != null)
                    new TextDecoder().decode(message);
                if (message.getEncodedImage() != null)
                    new ImageDecoder().decode(message);
                listeners.getMessageListener().onMessage(message);
                // chatPresenter.displayMessage(message);
            }
            else if(type.equals("user")){
                UserRequest user = gson.fromJson(jsonText, UserRequest.class);

                if(user.getAction().equals(UserRequest.ACTION_CREATE))
                    listeners.getAuthListener().onAuth(user);
                // loginPresenter.successAuth(user);
                if(user.getAction().equals(UserRequest.ACTION_UPDATE))
                    listeners.getUsernameUpdateListener().onUpdateUsername(user);
            }
            else if(type.equals("searchable_users")){
                SearchableUsers searchableUsers = gson.fromJson(jsonText, SearchableUsers.class);
                listeners.getSearchUserListener().displaySearchableUsers(searchableUsers);
            }
            else if(type.equals("checkingUsername")){
                CheckingUsername checkingUsername = gson.fromJson(jsonText, CheckingUsername.class);
                listeners.getUsernameUpdateListener().onResultUsernameCheck(checkingUsername);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() throws IOException {
        inputStream.close();
    }
}


