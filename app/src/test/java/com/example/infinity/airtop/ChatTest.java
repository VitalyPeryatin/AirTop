package com.example.infinity.airtop;

import com.example.infinity.airtop.data.db.interactors.ChatInteractor;
import com.example.infinity.airtop.data.db.model.Message;
import com.example.infinity.airtop.data.network.request.MessageRequest;
import com.example.infinity.airtop.data.prefs.app.AppPreferencesHelper;
import com.example.infinity.airtop.mocks.AppPreferenceMock;
import com.example.infinity.airtop.ui.chat.ChatPresenter;
import com.example.infinity.airtop.ui.chat.MessageBus;
import com.example.infinity.airtop.utils.MessageEditor;
import com.example.infinity.airtop.utils.ServerPostman;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ChatTest {

    @Mock
    private ChatInteractor interactor;
    @Mock
    private ServerPostman serverPostman;
    @Mock
    private MessageBus messageBus;
    @Mock
    private MessageEditor messageEditor;
    private AppPreferencesHelper preferencesHelper;
    private ChatPresenter chatPresenter;


    @Before
    public void init(){
        preferencesHelper = new AppPreferenceMock();
        chatPresenter = new ChatPresenter(interactor, serverPostman,
                messageBus, messageEditor, preferencesHelper);
    }

    @Test
    public void createWithTwoUUID(){
        String addresseeUUID = "46d53165-f5ec-4629-bd6e-dc72dd1cb0a9";
        String senderUUID = "6d84a8ef-ca4b-4a87-9413-60160026d2d8";
        chatPresenter.onCreate(addresseeUUID, senderUUID);
        verify(messageEditor).setAddressId(addresseeUUID);
        verify(messageEditor).setSenderId(senderUUID);
    }

    @Test
    public void createWithoutAddressUUID(){
        String addresseeUUID = "";
        String senderUUID = "6d84a8ef-ca4b-4a87-9413-60160026d2d8";
        chatPresenter.onCreate(addresseeUUID, senderUUID);
        verify(messageEditor).setAddressId(addresseeUUID);
        verify(messageEditor).setSenderId(senderUUID);
    }

    @Test
    public void createWithoutSenderUUID(){
        String addresseeUUID = "6d84a8ef-ca4b-4a87-9413-60160026d2d8";
        String senderUUID = "";
        chatPresenter.onCreate(addresseeUUID, senderUUID);
        verify(messageEditor).setAddressId(addresseeUUID);
        verify(messageEditor).setSenderId(senderUUID);
    }

    @Test
    public void subscribeMessageBus(){
        String addressUUID = "6d84a8ef-ca4b-4a87-9413-60160026d2d8";
        String senderUUID = "6d84a8ef-ca4b-4a87-9413-60160026d2d8";
        chatPresenter.onCreate(addressUUID, senderUUID);
        verify(messageBus).subscribe(chatPresenter);
    }

    @Test
    public void unsubscribeMessageBus(){
        chatPresenter.onDestroy();
        verify(messageBus).unsubscribe(chatPresenter);
    }

    @Test
    public void getSimpleSavedAdapterPosition(){
        String addressUUID = "6d84a8ef-ca4b-4a87-9413-60160026d2d8";
        int position = 1;
        int defaultPosition = 5;
        preferencesHelper.saveAdapterPosition(addressUUID, position);
        int gettedPos = chatPresenter.getAdapterPosition(addressUUID, defaultPosition);
        assertEquals(position, gettedPos);
    }

    @Test
    public void getZeroSavedAdapterPosition(){
        String addressUUID = "6d84a8ef-ca4b-4a87-9413-60160026d2d8";
        int position = 0;
        int defaultPosition = 5;
        preferencesHelper.saveAdapterPosition(addressUUID, position);
        int gettedPos = chatPresenter.getAdapterPosition(addressUUID, defaultPosition);
        assertEquals(defaultPosition, gettedPos);
    }

    @Test
    public void getSavedAdapterPositionByNull(){
        int position = 0;
        int defaultPosition = 5;
        preferencesHelper.saveAdapterPosition(null, position);
        int gettedPos = chatPresenter.getAdapterPosition(null, defaultPosition);
        assertEquals(defaultPosition, gettedPos);
    }

    @Test
    public void addSimpleTextToMessage(){
        String text = "Hello";
        chatPresenter.addTextToMessage(text);
        verify(messageEditor).addText(text);
    }

    @Test
    public void addEmptyTextToMessage(){
        String text = "";
        chatPresenter.addTextToMessage(text);
        verifyNoMoreInteractions(messageEditor);
    }

    @Test
    public void savePositiveAdapterPosition(){
        String addressUUID = "6d84a8ef-ca4b-4a87-9413-60160026d2d8";
        int position = 5;
        chatPresenter.saveAdapterPosition(addressUUID, position);
        assertEquals(position, preferencesHelper.getAdapterPosition(addressUUID));
    }

    @Test
    public void saveNegativeAdapterPosition(){
        String addressUUID = "6d84a8ef-ca4b-4a87-9413-60160026d2d8";
        int position = -2;
        chatPresenter.saveAdapterPosition(addressUUID, position);
        assertEquals(0, preferencesHelper.getAdapterPosition(addressUUID));
    }

    @Test
    public void sendEmptyMessage(){
        chatPresenter.sendMessage();
        verifyNoMoreInteractions(interactor);
        verifyNoMoreInteractions(serverPostman);
    }

    @Test
    public void sendTextMessage(){
        MessageRequest messageRequest = mock(MessageRequest.class);
        Message message = mock(Message.class);
        when(messageEditor.isNotEmptyMessage()).thenReturn(true);
        when(messageEditor.getMessage()).thenReturn(messageRequest);
        when(messageRequest.toMessageModel()).thenReturn(message);

        chatPresenter.sendMessage();
        verify(messageEditor).isNotEmptyMessage();
        verify(interactor).insertMessage(message);
        verify(messageEditor).clear();
        verify(serverPostman).postRequest(messageRequest);
    }

    @Test
    public void getNullNickname(){
        chatPresenter.getNickname();
        verify(interactor).getNicknameById(null);
    }

    @Test
    public void getNickname(){
        String addressUUID = "46d53165-f5ec-4629-bd6e-dc72dd1cb0a9";
        String senderUUID = "6d84a8ef-ca4b-4a87-9413-60160026d2d8";
        chatPresenter.onCreate(addressUUID, senderUUID);
        chatPresenter.getNickname();
        verify(interactor).getNicknameById(addressUUID);
    }
}
