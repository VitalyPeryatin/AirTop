package com.example.infinity.airtop.utils.serverWorker;

import java.util.ArrayList;

public class TestLauncherServerSending implements LauncherServerSending {

    private ArrayList<String> jsonMessages = new ArrayList<>();

    public TestLauncherServerSending(){
        jsonMessages.clear();
    }

    @Override
    public void sendMessage(String json) {
        jsonMessages.add(json);
    }

    public ArrayList<String> getAllMessages(){
        return jsonMessages;
    }

    public String getLastMessage(){
        return jsonMessages.get(jsonMessages.size() - 1);
    }

    public void clearMessage(){
        jsonMessages.clear();
    }
}
