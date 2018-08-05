package com.example.infinity.airtop.utils.serverWorker;

import com.example.infinity.airtop.utils.TestServer;

public class TestLauncherServerSending implements LauncherServerSending {

    private TestServer testServer;

    public TestLauncherServerSending(){
        testServer = TestServer.getInstance();
    }

    @Override
    public void sendMessage(String json) {
        testServer.addMessage(json);
    }
}
