package com.infinity_coder.infinity.airtop.data.network.response;

import com.infinity_coder.infinity.airtop.data.db.model.Contact;

public class AddressResponse implements ResponseModel{
    private Contact contact;

    public Contact getContact() {
        return contact;
    }
}
