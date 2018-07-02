package com.example.infinity.airtop.controller.client.readData;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import com.example.infinity.airtop.controller.client.BackendClient;
import com.example.infinity.airtop.model.Message;
import com.example.infinity.airtop.view.ChatActivity;
import com.example.infinity.airtop.view.RecyclerViewAdapter;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

public class ReadData extends AsyncTask<Void, String, Void>{

    private BackendClient backendClient;
    @SuppressLint("StaticFieldLeak")
    private Context context;
    private DataInputStream din;

    public ReadData(Context context, BackendClient backendClient, Socket socket) throws IOException {
        this.backendClient = backendClient;
        this.context = context;
        din = new DataInputStream(socket.getInputStream());
    }

    private synchronized void closeConnection(){
        try {
            din.close();
        } catch (IOException ignored) {}
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        RecyclerViewAdapter recyclerAdapter = ((RecyclerViewAdapter)((ChatActivity)context).recyclerView.getAdapter());
        Message message = new Message();

        message.setText(values[0]);

        assert recyclerAdapter != null;
        recyclerAdapter.getCurrentMessages().add(message);
        recyclerAdapter.notifyDataSetChanged();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        while(backendClient.isNotQuit()){
            try {
                String text = din.readUTF();
                publishProgress(text);
            }
            catch (SocketException ignored){}
            catch (IOException e) {e.printStackTrace();}
        }
        closeConnection();
        return null;
    }

}


