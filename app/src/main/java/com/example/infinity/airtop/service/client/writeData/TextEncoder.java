package com.example.infinity.airtop.service.client.writeData;

import com.example.infinity.airtop.data.network.MessageRequest;

/**
 * Class for encode text (If necessary)
 * @author infinity_coder
 * @version 1.0.2
 */
public class TextEncoder implements IEncoder {

    @Override
    public void encode(MessageRequest msg) {
        msg.setText(msg.getText().trim());
    }
}
