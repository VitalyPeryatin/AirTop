package com.infinity_coder.infinity.airtop.service.client;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.infinity_coder.infinity.airtop.App;
import com.infinity_coder.infinity.airtop.data.db.interactors.ChatInteractor;
import com.infinity_coder.infinity.airtop.data.db.interactors.ContactsInteractor;
import com.infinity_coder.infinity.airtop.data.db.model.Addressee;
import com.infinity_coder.infinity.airtop.data.db.model.Message;
import com.infinity_coder.infinity.airtop.data.network.request.UpdateBioRequest;
import com.infinity_coder.infinity.airtop.data.network.response.AddressResponse;
import com.infinity_coder.infinity.airtop.data.network.response.MessageResponse;
import com.infinity_coder.infinity.airtop.data.network.response.NicknameAuthResponse;
import com.infinity_coder.infinity.airtop.data.network.response.PhoneAuthResponse;
import com.infinity_coder.infinity.airtop.data.network.response.SearchUserResponse;
import com.infinity_coder.infinity.airtop.data.network.response.updaters.UpdateBioResponse;
import com.infinity_coder.infinity.airtop.data.network.response.updaters.UpdateNameResponse;
import com.infinity_coder.infinity.airtop.data.network.response.updaters.UpdateUsernameResponse;
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
    private ContactsInteractor contactsInteractor;

    // Constants for pass to handler the type of response
    private static final int
            MESSAGE_RESPONSE_KEY = 1,
            SEARCHABLE_USERS_KEY = 2,
            PHONE_AUTH_KEY = 3,
            NICKNAME_AUTH_KEY = 4,
            ADDRESSEE_KEY = 5,
            UPDATE_USERNAME_KEY = 6,
            UPDATE_NAME_KEY = 7,
            UPDATE_BIO_KEY = 8;


    DataReader(ServerConnection serverConnection) {
        this.serverConnection = serverConnection;
        responseListeners = App.getInstance().getResponseListeners();
        chatInteractor = new ChatInteractor();
        contactsInteractor = new ContactsInteractor();
    }

    public void connectToSocket(Socket socket){
        try {
            inputStream = new DataInputStream(socket.getInputStream());
        } catch (IOException e) { e.printStackTrace(); }
    }

    @SuppressWarnings("InfiniteLoopStatement")
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
                case "MessageRequest":
                    MessageResponse messageResponse = gson.fromJson(jsonText, MessageResponse.class);
                    messageResponse.decode();

                    message.what = MESSAGE_RESPONSE_KEY;
                    message.obj = messageResponse.toMessageModel();
                    break;
                case "phone_auth":
                    try {
                        PhoneAuthResponse phoneAuthResponse = gson.fromJson(jsonText, PhoneAuthResponse.class);
                        message.what = PHONE_AUTH_KEY;
                        message.obj =  phoneAuthResponse;
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case "update_username":
                    UpdateUsernameResponse usernameResponse = gson.fromJson(jsonText, UpdateUsernameResponse.class);
                    message.what = UPDATE_USERNAME_KEY;
                    message.obj = usernameResponse;
                    break;
                case "update_name":
                    UpdateNameResponse nameResponse = gson.fromJson(jsonText, UpdateNameResponse.class);
                    message.what = UPDATE_NAME_KEY;
                    message.obj = nameResponse;
                    break;
                case "update_bio":
                    UpdateBioResponse bioResponse = gson.fromJson(jsonText, UpdateBioResponse.class);
                    message.what = UPDATE_BIO_KEY;
                    message.obj = bioResponse;
                    break;
                case "nickname_auth":
                    NicknameAuthResponse nicknameAuthResponse = gson.fromJson(jsonText, NicknameAuthResponse.class);
                    message.what = NICKNAME_AUTH_KEY;
                    message.obj =  nicknameAuthResponse;
                    break;
                case "searchable_users":
                    SearchUserResponse searchUserResponse = gson.fromJson(jsonText, SearchUserResponse.class);
                    message.what = SEARCHABLE_USERS_KEY;
                    message.obj = searchUserResponse;
                    break;
                case "addressee":
                    AddressResponse addressResponse = gson.fromJson(jsonText, AddressResponse.class);
                    message.what = ADDRESSEE_KEY;
                    message.obj = addressResponse;
                    break;
                case "user_update":
                    AddressResponse userUpdateResponse = gson.fromJson(jsonText, AddressResponse.class);
                    Addressee addressee = userUpdateResponse.getAddressee();
                    chatInteractor.insertAddressee(addressee);
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
                    if(msg.obj instanceof Message) {
                        Message message = (Message) msg.obj;
                        chatInteractor.insertMessage(message);
                        responseListeners.getMessageBus().onMessage(message);
                    }
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
                        responseListeners.getPhoneAuthBus().onPhoneVerify((PhoneAuthResponse) msg.obj);
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
                        responseListeners.getUsernameSettingsBus().onUpdateSettings((UpdateUsernameResponse) msg.obj);
                    else
                        Log.e("mLogError", "DataReader -> не верный тип объекта. Ожидалось: UpdateUsernameResponse");
                    break;
                case UPDATE_NAME_KEY:
                    if(msg.obj instanceof UpdateNameResponse)
                        responseListeners.getNameSettingsBus().onUpdateSettings((UpdateNameResponse) msg.obj);
                    else
                        Log.e("mLogError", "DataReader -> не верный тип объекта. Ожидалось: UpdateNameResponse");
                    break;
                case UPDATE_BIO_KEY:
                    if(msg.obj instanceof UpdateBioResponse)
                        responseListeners.getBioSettingsBus().onUpdateSettings((UpdateBioResponse) msg.obj);
                    else
                        Log.e("mLogError", "DataReader -> не верный тип объекта. Ожидалось: UpdateBioResponse");
                    break;
                case ADDRESSEE_KEY:
                    if(msg.obj instanceof AddressResponse)
                        responseListeners.getMessageBus().onAddresse((AddressResponse) msg.obj);
                    else
                        Log.e("mLogError", "DataReader -> не верный тип объекта. Ожидалось: AddresseResponse");
                    break;
            }
        }
    };

    public void closeConnection() throws IOException {
        inputStream.close();
    }
}


