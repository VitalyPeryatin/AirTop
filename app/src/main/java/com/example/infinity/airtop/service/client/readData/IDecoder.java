package com.example.infinity.airtop.service.client.readData;

import com.example.infinity.airtop.data.network.Message;

public interface IDecoder {
    void decode(Message msg);
}
