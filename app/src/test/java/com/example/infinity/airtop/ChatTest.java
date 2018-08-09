package com.example.infinity.airtop;

import com.example.infinity.airtop.data.db.interactors.ChatInteractor;
import com.example.infinity.airtop.data.db.model.Message;
import com.example.infinity.airtop.data.prefs.app.AppPreferencesHelper;
import com.example.infinity.airtop.ui.chat.ChatPresenter;
import com.example.infinity.airtop.ui.chat.MessageBus;
import com.example.infinity.airtop.utils.MessageEditor;
import com.example.infinity.airtop.utils.serverWorker.IServerPostman;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
@Config(constants = BuildConfig.class)
public class ChatTest {

    @InjectMocks
    private ChatPresenter presenter;
    private MessageEditor messageEditor;

    @Before
    public void init(){
        presenter = new ChatPresenter();
    }

    @Test
    public void createPresenterTwoId(){

        String addressId = "45745c60-7b1a-11e8-9c9c-2d42b21b1a3e";
        String senderId = "1h423j60-7b1a-324h-9c9c-1238jhj234j3";
        presenter.onCreate(addressId, senderId);
        Message message = presenter.getMessageEditor().getMessage().toMessageModel();
        assertEquals(addressId, message.addressId);
        assertEquals(senderId, message.senderId);
    }

    @Test
    public void createPresenterSenderId(){
        String addressId = "45745c60-7b1a-11e8-9c9c-2d42b21b1a3e";
        String senderId = "1h423j60-7b1a-324h-9c9c-1238jhj234j3";
        presenter.onCreate(addressId, senderId);
        Message message = presenter.getMessageEditor().getMessage().toMessageModel();
        assertEquals(addressId, message.addressId);
        assertEquals(senderId, message.senderId);
    }
}
