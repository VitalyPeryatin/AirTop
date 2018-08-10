package com.example.infinity.airtop;

import com.example.infinity.airtop.data.db.interactors.ChatInteractor;
import com.example.infinity.airtop.data.db.model.User;
import com.example.infinity.airtop.data.network.response.MessageResponse;
import com.example.infinity.airtop.data.network.response.PhoneAuthResponse;
import com.example.infinity.airtop.data.prefs.auth.AuthPreferencesHelper;
import com.example.infinity.airtop.ui.auth.phone.PhoneAuthBus;
import com.example.infinity.airtop.ui.auth.phone.PhoneAuthPresenter;
import com.example.infinity.airtop.utils.ServerPostman;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PhoneAuthTest {

    @Mock
    private ChatInteractor interactor;
    @Mock
    private ServerPostman serverPostman;
    @Mock
    private PhoneAuthBus phoneAuthBus;
    @Mock
    private AuthPreferencesHelper preferencesHelper;

    private PhoneAuthPresenter presenter;

    @Before
    public void init(){
        presenter = new PhoneAuthPresenter(interactor, serverPostman, phoneAuthBus, preferencesHelper);
    }

    @Test
    public void onCreate(){
        presenter.onCreate();
        verify(phoneAuthBus).subscribe(presenter);
    }

    @Test
    public void onDestroy(){
        presenter.onDestroy();
        verify(phoneAuthBus).unsubscribe(presenter);
    }

    @Test
    public void onPhoneAuthResultOk(){
        String phone = "+79035724917";
        PhoneAuthResponse response = mock(PhoneAuthResponse.class);
        when(response.getResult()).thenReturn("RESULT_OK");
        presenter.sendPhone(phone);
        presenter.onPhoneAuth(response);

        verify(preferencesHelper).saveCurrentPhone(phone);
    }

    @Test
    public void onPhoneAuthResultExists(){
        String phone = "+79035724917";
        PhoneAuthResponse response = mock(PhoneAuthResponse.class);
        User user = mock(User.class);
        when(response.getResult()).thenReturn("RESULT_EXISTS");
        when(response.getUser()).thenReturn(user);
        presenter.sendPhone(phone);
        presenter.onPhoneAuth(response);

        verify(preferencesHelper).saveCurrentPhone(phone);
        verify(preferencesHelper).saveUserHasNickname(true);
        verify(interactor).insertUser(user);
    }
}
