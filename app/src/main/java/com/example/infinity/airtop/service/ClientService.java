package com.example.infinity.airtop.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.infinity.airtop.service.client.ServerConnection;

/**
 * Service for launching "ServerConnection" and managing queries to server
 * @author infinity_coder
 * @version 1.0.2
 */
public class ClientService extends Service {

    private ServerConnection serverConnection;

    @Override
    public void onCreate() {
        super.onCreate();
        serverConnection = new ServerConnection();
        serverConnection.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String json;

        // If there is a json-request to server, the data is sent
        if((json = intent.getStringExtra("request")) != null)
            serverConnection.sendRequest(json);

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
