package com.example.infinity.airtop.utils.serverWorker;

import com.example.infinity.airtop.data.network.request.RequestModel;
import com.example.infinity.airtop.utils.TestServer;

public class TestIServerPostman implements IServerPostman {

    private TestServer testServer;

    public TestIServerPostman(){
        testServer = TestServer.getInstance();
    }

    @Override
    public void postRequest(RequestModel request) {
        testServer.addMessage(request.toJson());
    }
}
