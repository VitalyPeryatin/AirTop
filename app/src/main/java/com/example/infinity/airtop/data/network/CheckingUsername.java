package com.example.infinity.airtop.data.network;

public class CheckingUsername implements RequestModel {
    public static final String RESULT_OK = "ok", RESULT_CANCEL = "cancel",
            RESULT_LITTLE = "little", RESULT_EMPTY = "empty";
    private final String TYPE = TYPE_CHECK_USERNAME;

    private String username, result;

    public CheckingUsername(String username){
        this.username =  username;
    }

    public CheckingUsername(){

    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }
}
