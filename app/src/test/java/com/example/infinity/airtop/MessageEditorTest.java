package com.example.infinity.airtop;


import com.example.infinity.airtop.data.db.model.Message;
import com.example.infinity.airtop.utils.MessageEditor;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class MessageEditorTest {
    private MessageEditor messageEditor;


    @Test
    public void testAddingRussianText(){
        String text = "Привет из Зеландии";
        messageEditor = MessageEditor.edit();
        messageEditor.addText(text);
        Message message = messageEditor.getMessage().toMessageModel();
        assertEquals(text, message.text);
    }

    @Test
    public void testAddingEnglishText(){
        String text = "Hello by friends";
        messageEditor = MessageEditor.edit();
        messageEditor.addText(text);
        Message message = messageEditor.getMessage().toMessageModel();
        assertEquals(text, message.text);
    }

    @Test
    public void setSenderUUID(){
        String uuid = "123e4567-e89b-12d3-a456-426655440000";
        messageEditor = MessageEditor.edit();
        messageEditor.setSenderId(uuid);
        Message message = messageEditor.getMessage().toMessageModel();
        assertEquals(uuid, message.senderId);
    }

    @Test
    public void setAddressUUID(){
        String uuid = "123e4567-e89b-12d3-a456-426655440000";
        messageEditor = MessageEditor.edit();
        messageEditor.setAddressId(uuid);
        Message message = messageEditor.getMessage().toMessageModel();
        assertEquals(uuid, message.addressId);
    }

    @Test
    public void setAll(){
        String addressUUID = "123e4567-e89b-12d3-a456-426655440000";
        String senderUUID = "145e7584-q87b-13g3-a456-428327468720";
        String text = "text";
        messageEditor = MessageEditor.edit();
        messageEditor.setAddressId(addressUUID);
        messageEditor.setSenderId(senderUUID);
        messageEditor.addText(text);
        Message message = messageEditor.getMessage().toMessageModel();
        assertEquals(addressUUID, message.addressId);
        assertEquals(senderUUID, message.senderId);
        assertEquals(text, message.text);
    }
}
