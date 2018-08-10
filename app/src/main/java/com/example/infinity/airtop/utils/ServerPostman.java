package com.example.infinity.airtop.utils;

import android.content.Context;
import android.content.Intent;

import com.example.infinity.airtop.App;
import com.example.infinity.airtop.data.network.request.RequestModel;
import com.example.infinity.airtop.service.ClientService;

public class ServerPostman {
    public void postRequest(RequestModel request) {
        Context context = App.getInstance().getBaseContext();

        Intent intent = new Intent(context, ClientService.class);
        intent.putExtra("request", request.toJson());
        context.startService(intent);
    }
}
