package com.example.infinity.airtop.services.client.writeData;

import com.example.infinity.airtop.models.Message;

public class TextEncoder implements IEncoder {

    @Override
    public void encode(Message msg) {
        msg.setText(msg.getText().trim());
    }
}
