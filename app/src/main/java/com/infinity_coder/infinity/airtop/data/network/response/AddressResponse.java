package com.infinity_coder.infinity.airtop.data.network.response;

import com.infinity_coder.infinity.airtop.data.db.model.Addressee;

public class AddressResponse implements ResponseModel{
    private Addressee addressee;

    public Addressee getAddressee() {
        return addressee;
    }
}
