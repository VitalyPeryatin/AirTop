package com.example.infinity.airtop;

import com.example.infinity.airtop.ui.chat.ChatPresenter;
import com.example.infinity.airtop.utils.TestServer;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class SendingMessageTest {

    private TestServer testServer;
    private String addressPhone = "89035724917", senderPhone = "89688512558";
    private ChatPresenter chatPresenter;

    @Before
    public void init(){
        chatPresenter = new ChatPresenter();
        chatPresenter.onCreate(addressPhone, senderPhone);
        testServer = TestServer.getInstance();
    }

    @Test
    public void sendSimpleMessage(){
        String messageText = "Hello";
        String jsonMessage = String.format("82@{\"text\":\"%s\",\"sender\":\"%s\",\"addressee\":\"%s\",\"TYPE\":\"message\"}",
                messageText, senderPhone, addressPhone);
        chatPresenter.getMessageEditor().addText(messageText);
        chatPresenter.sendMessage();
        assertEquals(jsonMessage, testServer.getLastMessage());
    }

    @Test
    public void sendEmptyMessage(){
        String messageText = "";
        String jsonMessage = String.format("77@{\"text\":\"%s\",\"sender\":\"%s\",\"addressee\":\"%s\",\"TYPE\":\"message\"}",
                messageText, senderPhone, addressPhone);

        chatPresenter.getMessageEditor().addText(messageText);
        chatPresenter.sendMessage();
        assertEquals(jsonMessage, testServer.getLastMessage());
    }
}
