package com.infinity_coder.infinity.airtop.data.network.request;

import java.util.ArrayList;

public class SubscribeUserUpdateRequest extends RequestModel {
    private ArrayList<String> uuids;

    public SubscribeUserUpdateRequest(ArrayList<String> uuids) {
        this.uuids = uuids;
    }
}
