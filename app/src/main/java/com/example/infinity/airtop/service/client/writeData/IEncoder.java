package com.example.infinity.airtop.service.client.writeData;

import com.example.infinity.airtop.data.network.MessageRequest;

public interface IEncoder {
    void encode(MessageRequest msg);
}
