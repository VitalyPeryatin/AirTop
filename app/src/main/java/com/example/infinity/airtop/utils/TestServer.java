package com.example.infinity.airtop.utils;

import java.util.ArrayList;

public class TestServer {
    private static TestServer instance;
    private ArrayList<String> jsonMessages = new ArrayList<>();

    private TestServer(){}

    public static TestServer getInstance() {
        if(instance == null) instance = new TestServer();
        return instance;
    }

    public void addMessage(String json){
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
