package com.infinity_coder.infinity.airtop.utils;

import android.content.Context;
import android.content.Intent;

import com.infinity_coder.infinity.airtop.App;
import com.infinity_coder.infinity.airtop.data.network.request.RequestModel;
import com.infinity_coder.infinity.airtop.service.ClientService;

public class ServerPostman {
    public void postRequest(RequestModel request) {
        Context context = App.getInstance().getBaseContext();

        Intent intent = new Intent(context, ClientService.class);
        intent.putExtra("request", request.toJson());
        context.startService(intent);
    }
}
