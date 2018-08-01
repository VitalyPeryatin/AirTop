package com.example.infinity.airtop.service.client.writeData;

import com.example.infinity.airtop.data.network.Message;

public interface IEncoder {
    void encode(Message msg);
}
