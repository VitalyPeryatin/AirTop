package com.example.infinity.airtop.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.infinity.airtop.services.client.readData.DataReader;
import com.example.infinity.airtop.views.App;

public class ReaderService extends Service{
    DataReader reader;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        reader = new DataReader(App.getInstance().getBackendClient());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }
}
