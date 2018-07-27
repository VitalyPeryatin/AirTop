package com.example.infinity.airtop.presenters.client.readData;

import android.util.Log;

import com.example.infinity.airtop.models.SearchableUsers;
import com.example.infinity.airtop.models.User;
import com.example.infinity.airtop.presenters.ChatPresenter;
import com.example.infinity.airtop.presenters.LoginPresenter;
import com.example.infinity.airtop.presenters.SearchUserPresenter;
import com.example.infinity.airtop.presenters.client.BackendClient;
import com.example.infinity.airtop.models.Message;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;


/**
 * Class for reading the received data and publishing on the activity
 * @author infinity_coder
 * @version 1.0.0
 */
public class DataReader extends Thread{
    private BackendClient backendClient; // object for controlling of closing connection
    private DataInputStream inputStream;
    private ChatPresenter chatPresenter;
    private LoginPresenter loginPresenter;
    private SearchUserPresenter searchUserPresenter;

    public DataReader(BackendClient backendClient, Socket socket) throws IOException {
        this.backendClient = backendClient;
        inputStream = new DataInputStream(socket.getInputStream());
        chatPresenter = ChatPresenter.getInstance();
        loginPresenter = LoginPresenter.getInstance();
        searchUserPresenter = SearchUserPresenter.getInstance();
    }

    @Override
    public void run() {
        super.run();
        //while(backendClient.isNotQuit())
        try {
            while(true) {
                publishMessage(readData());
            }
        } catch (IOException e) {
            e.printStackTrace();
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
            String type = jsonObject.getString("TYPE");
            if(type.equals("message")) {
                Message message = gson.fromJson(jsonText, Message.class);
                if (message.getText() != null)
                    new TextDecoder().decode(message);
                if (message.getEncodedImage() != null)
                    new ImageDecoder().decode(message);
                chatPresenter.displayMessage(message);
            }
            else if(type.equals("user")){
                User user = gson.fromJson(jsonText, User.class);
                loginPresenter.successAuth(user);
            }
            else if(type.equals("searchable_users")){
                Log.d("mLog", jsonText);
                SearchableUsers searchableUsers = gson.fromJson(jsonText, SearchableUsers.class);
                searchUserPresenter.displaySearchableUsers(searchableUsers);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}


