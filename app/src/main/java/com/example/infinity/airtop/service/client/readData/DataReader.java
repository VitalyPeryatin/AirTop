package com.example.infinity.airtop.service.client.readData;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.infinity.airtop.data.db.interactors.ChatInteractor;
import com.example.infinity.airtop.data.db.model.Message;
import com.example.infinity.airtop.data.network.CheckingUsername;
import com.example.infinity.airtop.data.network.MessageRequest;
import com.example.infinity.airtop.data.network.SearchableUsers;
import com.example.infinity.airtop.data.network.UserRequest;
import com.example.infinity.airtop.service.client.ServerConnection;
import com.example.infinity.airtop.App;
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
 * @version 1.0.2
 */
public class DataReader extends Thread{
    private ServerConnection serverConnection;
    private DataInputStream inputStream;
    private App.ResponseListeners responseListeners;
    private ChatInteractor chatInteractor;

    // Constants for pass to handler the type of response
    private static final int
            MESSAGE_REQUEST_KEY = 1,
            USER_CREATE_KEY = 2,
            USER_UPDATE_KEY = 3,
            SEARCHABLE_USERS_KEY = 4,
            CHECKING_USERNAME_KEY = 5;

    public DataReader(ServerConnection serverConnection) {
        this.serverConnection = serverConnection;
        responseListeners = App.getInstance().getResponseListeners();
        chatInteractor = new ChatInteractor();
    }

    public void connectToSocket(Socket socket){
        try {
            inputStream = new DataInputStream(socket.getInputStream());
        } catch (IOException e) { e.printStackTrace(); }
    }

    @Override
    public void run() {
        super.run();
        while(true) {
            try {
                while (serverConnection.isQuit())
                    Thread.yield();
                try {
                    publishMessage(readData());
                } catch (SocketException se) {
                    serverConnection.reconnectToServer();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Bitwise receives data from the server
    private String readData() throws IOException {
        StringBuilder n = new StringBuilder();
        byte[] b = new byte[1];
        while ((b[0] = inputStream.readByte()) != (byte)'@')
            n.append(new String(b, 0, 1));
        int bytesLen = Integer.parseInt(n.toString());
        byte[] bytes = new byte[bytesLen];
        inputStream.readFully(bytes);
        return new String(bytes, serverConnection.getCharsetName());
    }


    // Converts the received string to the "Message" object and sends it to a publication at the Activity
    private void publishMessage(String jsonText){
        Gson gson = new Gson();
        try {
            JSONObject jsonObject = new JSONObject(jsonText);
            Log.d("mLog", "Сообщение принято: " + jsonText);
            String type = jsonObject.getString("TYPE");
            android.os.Message message = new android.os.Message();
            switch (type) {
                case "message":
                    MessageRequest messageRequest = gson.fromJson(jsonText, MessageRequest.class);
                    if (messageRequest.getText() != null)
                        new TextDecoder().decode(messageRequest);
                    if (messageRequest.getEncodedImage() != null)
                        new ImageDecoder().decode(messageRequest);

                    Message messageModel = new Message(messageRequest, Message.ROUTE_IN);
                    chatInteractor.insertMessage(messageModel);

                    message.what = MESSAGE_REQUEST_KEY;
                    message.obj = messageModel;
                    break;
                case "user":
                    UserRequest user = gson.fromJson(jsonText, UserRequest.class);

                    if (user.getAction().equals(UserRequest.ACTION_CREATE))
                        message.what = USER_CREATE_KEY;
                    if (user.getAction().equals(UserRequest.ACTION_UPDATE))
                        message.what = USER_UPDATE_KEY;
                    message.obj = user;
                    break;
                case "searchable_users":
                    SearchableUsers searchableUsers = gson.fromJson(jsonText, SearchableUsers.class);
                    message.what = SEARCHABLE_USERS_KEY;
                    message.obj = searchableUsers;
                    break;
                case "checkingUsername":
                    CheckingUsername checkingUsername = gson.fromJson(jsonText, CheckingUsername.class);
                    message.what = CHECKING_USERNAME_KEY;
                    message.obj = checkingUsername;
                    break;
            }
            handler.sendMessage(message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Notifies the necessary listeners by key in the UI thread
    private Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);

            switch (msg.what){
                case MESSAGE_REQUEST_KEY:
                    if(msg.obj instanceof Message)
                        responseListeners.getMessageBus().onMessage((Message) msg.obj);
                    else
                        Log.e("mLogError", "DataReader -> не верный тип объекта. Ожидалось: Message");
                    break;
                case USER_CREATE_KEY:
                    if(msg.obj instanceof UserRequest)
                        responseListeners.getAuthListener().onAuth((UserRequest) msg.obj);
                    else
                        Log.e("mLogError", "DataReader -> не верный тип объекта. Ожидалось: UserRequest");
                    break;
                case USER_UPDATE_KEY:
                    if(msg.obj instanceof UserRequest)
                        responseListeners.getUsernameUpdateListener().onUpdateUsername((UserRequest) msg.obj);
                    else
                        Log.e("mLogError", "DataReader -> не верный тип объекта. Ожидалось: UserRequest");
                    break;
                case SEARCHABLE_USERS_KEY:
                    if(msg.obj instanceof SearchableUsers)
                        responseListeners.getSearchUserListener().displaySearchableUsers((SearchableUsers) msg.obj);
                    else
                        Log.e("mLogError", "DataReader -> не верный тип объекта. Ожидалось: SearchableUsers");
                    break;
                case CHECKING_USERNAME_KEY:
                    if(msg.obj instanceof CheckingUsername)
                        responseListeners.getUsernameUpdateListener().onResultUsernameCheck((CheckingUsername) msg.obj);
                    else
                        Log.e("mLogError", "DataReader -> не верный тип объекта. Ожидалось: CheckingUsername");
                    break;
            }
        }
    };

    public void closeConnection() throws IOException {
        inputStream.close();
    }
}


