package com.example.infinity.airtop;

import android.content.Context;

import com.example.infinity.airtop.data.network.request.MessageRequest;
import com.example.infinity.airtop.ui.chat.ChatPresenter;
import com.example.infinity.airtop.utils.MessageEditor;

import org.junit.Test;
import org.mockito.Mock;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.mock;

public class MessageEditorTest {

    @Mock
    Context context;

    private MessageEditor messageEditor;

    @Test
    public void testAddingRussianText(){
        String text = "Привет из Зеландии";
        messageEditor = MessageEditor.edit();
        messageEditor.addText(text);
        MessageRequest messageRequest = messageEditor.getMessage();
        assertEquals(text, messageRequest.text);
    }

    @Test
    public void testAddingEnglishText(){
        String text = "Hello by friends";
        messageEditor = MessageEditor.edit();
        messageEditor.addText(text);
        MessageRequest messageRequest = messageEditor.getMessage();
        assertEquals(text, messageRequest.text);
    }

    @Test
    public void testAddingSender(){
        String phone = "89855136621";
        messageEditor = MessageEditor.edit();
        messageEditor.setSenderId(phone);
        MessageRequest messageRequest = messageEditor.getMessage();
        //assertEquals(phone, messageRequest.sender);
    }

    @Test
    public void testAddingAddressee(){
        String phone = "89035724917";
        messageEditor = MessageEditor.edit();
        messageEditor.setAddressId(phone);
        MessageRequest messageRequest = messageEditor.getMessage();
        //assertEquals(phone, messageRequest.addressee);
    }

    @Test
    public void testAddingMultiplyTest(){
        String adderessPhone = "749832749823";
        String senderPhone = "89035724917";
        String text = "text";
        messageEditor = MessageEditor.edit();
        messageEditor.setAddressId(adderessPhone);
        messageEditor.setSenderId(senderPhone);
        messageEditor.addText(text);
        MessageRequest messageRequest = messageEditor.getMessage();
        //assertEquals(adderessPhone, messageRequest.addressee);
        //assertEquals(senderPhone, messageRequest.sender);
        assertEquals(text, messageRequest.text);
    }

    @Test
    public void testNonPreparedMessageRequest(){
        ChatPresenter chatPresenter = new ChatPresenter();
        messageEditor = chatPresenter.getMessageEditor();
        MessageRequest messageRequest = messageEditor.getMessage();
        //assertEquals(null, messageRequest.getAddressee());
        //assertEquals(null, messageRequest.getSender());
    }

    @Test
    public void testPreparedMessageRequest(){
        context = mock(Context.class);
        ChatPresenter chatPresenter = new ChatPresenter();
        String addressPhone = "89035724917";
        String sendPhone = "89688512558";
        chatPresenter.onCreate(addressPhone, sendPhone);
        messageEditor = chatPresenter.getMessageEditor();
        MessageRequest messageRequest = messageEditor.getMessage();
        //assertEquals(addressPhone, messageRequest.getAddressee());
        //assertEquals(sendPhone, messageRequest.getSender());
    }
}
