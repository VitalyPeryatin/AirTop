package com.example.infinity.airtop.service.client;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.infinity.airtop.data.db.interactors.ChatInteractor;
import com.example.infinity.airtop.data.db.model.Message;
import com.example.infinity.airtop.data.network.request.SearchUserRequest;
import com.example.infinity.airtop.data.network.response.MessageResponse;
import com.example.infinity.airtop.data.network.response.NicknameAuthResponse;
import com.example.infinity.airtop.data.network.response.PhoneAuthResponse;
import com.example.infinity.airtop.data.network.response.SearchUserResponse;
import com.example.infinity.airtop.data.network.response.UpdateUsernameResponse;
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

    // TODO Срочно исправить говнокод!
    // Constants for pass to handler the type of response
    private static final int
            MESSAGE_RESPONSE_KEY = 1,
            SEARCHABLE_USERS_KEY = 2,
            PHONE_AUTH_KEY = 3,
            NICKNAME_AUTH_KEY = 4,
            UPDATE_USERNAME_KEY = 5;

    DataReader(ServerConnection serverConnection) {
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
                    MessageResponse messageResponse = gson.fromJson(jsonText, MessageResponse.class);
                    messageResponse.decode();

                    Message messageModel = new Message(messageResponse, Message.ROUTE_IN);
                    chatInteractor.insertMessage(messageModel);

                    message.what = MESSAGE_RESPONSE_KEY;
                    message.obj = messageModel;
                    break;
                case "phone_auth":
                    PhoneAuthResponse phoneAuthResponse = gson.fromJson(jsonText, PhoneAuthResponse.class);
                    message.what = PHONE_AUTH_KEY;
                    message.obj =  phoneAuthResponse;
                    break;
                case "update_username":
                    UpdateUsernameResponse response = gson.fromJson(jsonText, UpdateUsernameResponse.class);
                    message.what = UPDATE_USERNAME_KEY;
                    message.obj = response;
                    break;
                case "nickname_auth":
                    NicknameAuthResponse nicknameAuthResponse = gson.fromJson(jsonText, NicknameAuthResponse.class);
                    message.what = NICKNAME_AUTH_KEY;
                    message.obj =  nicknameAuthResponse;
                    break;
                case "searchable_users":
                    SearchUserResponse SearchUserResponse = gson.fromJson(jsonText, SearchUserResponse.class);
                    message.what = SEARCHABLE_USERS_KEY;
                    message.obj = SearchUserResponse;
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
                case MESSAGE_RESPONSE_KEY:
                    if(msg.obj instanceof Message)
                        responseListeners.getMessageBus().onMessage((Message) msg.obj);
                    else
                        Log.e("mLogError", "DataReader -> не верный тип объекта. Ожидалось: Message");
                    break;
                case SEARCHABLE_USERS_KEY:
                    if(msg.obj instanceof SearchUserResponse)
                        responseListeners.getSearchUserBus().displaySearchableUsers((SearchUserResponse) msg.obj);
                    else
                        Log.e("mLogError", "DataReader -> не верный тип объекта. Ожидалось: SearchableUsers");
                    break;
                case PHONE_AUTH_KEY:
                    if(msg.obj instanceof PhoneAuthResponse)
                        responseListeners.getPhoneAuthBus().onPhoneAuth((PhoneAuthResponse) msg.obj);
                    else
                        Log.e("mLogError", "DataReader -> не верный тип объекта. Ожидалось: PhoneAuthResponse");
                    break;
                case NICKNAME_AUTH_KEY:
                    if(msg.obj instanceof NicknameAuthResponse)
                        responseListeners.getNicknameAuthBus().onNicknameAuth((NicknameAuthResponse) msg.obj);
                    else
                        Log.e("mLogError", "DataReader -> не верный тип объекта. Ожидалось: NicknameAuthResponse");
                    break;
                case UPDATE_USERNAME_KEY:
                    if(msg.obj instanceof UpdateUsernameResponse)
                        responseListeners.getUsernameUpdateBus().onUpdateUsername((UpdateUsernameResponse) msg.obj);
                    else
                        Log.e("mLogError", "DataReader -> не верный тип объекта. Ожидалось: UpdateUsernameResponse");
                    break;
            }
        }
    };

    public void closeConnection() throws IOException {
        inputStream.close();
    }
}


