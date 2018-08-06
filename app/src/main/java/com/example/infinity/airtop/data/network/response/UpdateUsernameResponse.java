package com.example.infinity.airtop.data.network.response;

public class UpdateUsernameResponse {
    public static String RESULT_OK = "RESULT_OK", RESULT_CANCEL = "RESULT_CANCEL";
    private String TYPE;
    private String result;
    private String phone;
    private String username;

    public String getTYPE() {
        return TYPE;
    }

    public String getResult() {
        return result;
    }

    public String getPhone() {
        return phone;
    }

    public String getUsername() {
        return username;
    }
}
