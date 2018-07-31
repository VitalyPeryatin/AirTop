package com.example.infinity.airtop.services.client.writeData;

import com.example.infinity.airtop.models.Message;

public interface IEncoder {
    void encode(Message msg);
}
