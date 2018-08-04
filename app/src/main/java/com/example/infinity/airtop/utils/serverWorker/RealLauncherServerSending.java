package com.example.infinity.airtop.utils.serverWorker;

import android.content.Context;
import android.content.Intent;

import com.example.infinity.airtop.App;
import com.example.infinity.airtop.service.ClientService;

public class RealLauncherServerSending implements LauncherServerSending {
    @Override
    public void sendMessage(String json) {
        Context context = App.getInstance().getBaseContext();

        Intent intent = new Intent(context, ClientService.class);
        intent.putExtra("request", json);
        context.startService(intent);
    }
}
