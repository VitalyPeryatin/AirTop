package com.example.infinity.airtop.service.client.writeData;

import com.example.infinity.airtop.data.network.Message;

public class TextEncoder implements IEncoder {

    @Override
    public void encode(Message msg) {
        msg.setText(msg.getText().trim());
    }
}
