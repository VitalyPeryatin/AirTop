package com.example.infinity.airtop.controller.client.readData;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;

import com.example.infinity.airtop.controller.client.BackendClient;
import com.example.infinity.airtop.model.Message;
import com.example.infinity.airtop.view.ChatActivity;
import com.example.infinity.airtop.view.MessageListViewAdapter;
import com.google.gson.Gson;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;


/**
 * Class for reading the received data and publishing on the activity
 * @author infinity_coder
 * @version 1.0.0
 */
public class DataReader extends AsyncTask<Void, Message, Void>{
    @SuppressLint("StaticFieldLeak")
    private Context context;
    private BackendClient backendClient; // object for controlling of closing connection
    private DataInputStream inputStream;

    public DataReader(BackendClient backendClient, Socket socket, Context context) throws IOException {
        this.backendClient = backendClient;
        this.context = context;
        inputStream = new DataInputStream(socket.getInputStream());
    }

    @Override
    protected void onProgressUpdate(Message... values) {
        super.onProgressUpdate(values);

        // Find recyclerAdapter (it contains list of current messages) and add the next message
        RecyclerView msgListView = ((ChatActivity)context).getMsgListView();
        MessageListViewAdapter recyclerAdapter = (MessageListViewAdapter) msgListView.getAdapter();
        assert recyclerAdapter != null;
        recyclerAdapter.getCurrentMessages().add(values[0]);
        recyclerAdapter.notifyDataSetChanged();
        msgListView.scrollToPosition(recyclerAdapter.getItemCount() - 1);
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
     * @param jsonMessage json-string for transformation to "Message" object and following publication
     */
    private void publishMessage(String jsonMessage){
        Gson gson = new Gson();
        Message message = gson.fromJson(jsonMessage, Message.class);
        if(message.getText() != null)
            new TextDecoder().decode(message);
        if(message.getEncodedImage() != null)
            new ImageDecoder().decode(message);
        publishProgress(message);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            //while(backendClient.isNotQuit())
            while(true)
                publishMessage(readData());

        }
        catch (SocketException ignored){
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            backendClient.closeConnection();
        }
        catch (Exception e) {
            backendClient.reloadServer();

        }
        return null;
    }
}


