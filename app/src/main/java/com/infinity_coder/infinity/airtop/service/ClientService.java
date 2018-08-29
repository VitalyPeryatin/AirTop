package com.infinity_coder.infinity.airtop.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.infinity_coder.infinity.airtop.service.client.ServerConnection;

/**
 * Service for launching "ServerConnection" and managing queries to server
 * @author infinity_coder
 * @version 1.0.4
 */
public class ClientService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        ServerConnection.getNewInstance().start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        ServerConnection.getInstance().closeConnection(false);
        super.onDestroy();
    }
}
