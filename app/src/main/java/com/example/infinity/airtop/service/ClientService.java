package com.example.infinity.airtop.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.infinity.airtop.service.client.BackendClient;

public class ClientService extends Service {

    private BackendClient backendClient;

    @Override
    public void onCreate() {
        super.onCreate();
        backendClient = new BackendClient();
        backendClient.start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String json;
        if((json = intent.getStringExtra("request")) != null) {
            backendClient.sendRequest(json);
        }
        return START_STICKY;
    }
}
