package com.example.infinity.airtop;

import com.example.infinity.airtop.ui.chat.ChatPresenter;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RestoringMessageAdapterPositionTest {
    private ChatPresenter chatPresenter;
    private String addressPhone = "89688512558";

    @Before
    public void init(){
        chatPresenter = new ChatPresenter();
        String sendPhone = "89035724917";
        chatPresenter.onCreate(addressPhone, sendPhone);
    }

    @Test
    public void restoreSimplePosition(){
        int position = 20;
        chatPresenter.saveAdapterPosition(addressPhone, position);
        //assertEquals(position, chatPresenter.getAdapterPosition(addressPhone));
    }

    @Test
    public void restoreNegativePosition(){
        int position = -1;
        chatPresenter.saveAdapterPosition(addressPhone, position);
        //assertEquals(0, chatPresenter.getAdapterPosition(addressPhone));
    }

    @Test
    public void restoreZeroePosition(){
        int position = 0;
        chatPresenter.saveAdapterPosition(addressPhone, position);
        //assertEquals(position, chatPresenter.getAdapterPosition(addressPhone));
    }


}
