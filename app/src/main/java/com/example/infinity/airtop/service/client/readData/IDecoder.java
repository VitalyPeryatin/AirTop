package com.example.infinity.airtop.service.client.readData;

import com.example.infinity.airtop.data.network.MessageRequest;

public interface IDecoder {
    void decode(MessageRequest msg);
}
