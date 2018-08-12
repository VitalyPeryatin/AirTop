package com.example.infinity.airtop.ui.auth.phone;

import android.telephony.PhoneNumberUtils;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.infinity.airtop.data.db.interactors.ChatInteractor;
import com.example.infinity.airtop.data.network.request.PhoneAuthRequest;
import com.example.infinity.airtop.data.network.response.PhoneAuthResponse;
import com.example.infinity.airtop.data.prefs.app.AppPreference;
import com.example.infinity.airtop.data.prefs.auth.AuthPreference;
import com.example.infinity.airtop.utils.ServerPostman;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@InjectViewState
public class PhoneAuthPresenter extends MvpPresenter<PhoneAuthView> implements OnPhoneAuthListener{

    private ChatInteractor interactor;
    private ServerPostman serverPostman;
    private PhoneAuthBus phoneAuthBus;
    private AuthPreference preferencesHelper;
    private String phone;

    public PhoneAuthPresenter(ChatInteractor interactor, ServerPostman serverPostman,
                              PhoneAuthBus phoneAuthBus, AuthPreference preferencesHelper){
        this.interactor = interactor;
        this.serverPostman = serverPostman;
        this.phoneAuthBus = phoneAuthBus;
        this.preferencesHelper = preferencesHelper;
    }

    public void onCreate(){
        phoneAuthBus.subscribe(this);
    }

    public void sendPhone(String phone){
        this.phone = phone;
        if(isValidPhone(phone)) {
            serverPostman.postRequest(new PhoneAuthRequest(phone));
        }
        else{
            getViewState().notValidPhone();
        }
    }

    @Override
    public void onPhoneAuth(PhoneAuthResponse response) {
        if(response.getResult().equals("RESULT_OK")){
            preferencesHelper.saveCurrentPhone(phone);
        }
        else if (response.getResult().equals("RESULT_EXISTS")){
            preferencesHelper.saveCurrentPhone(phone);
            preferencesHelper.saveUserHasNickname(true);
            interactor.insertUser(response.getUser());
        }
        getViewState().successfulPhoneAuth();
    }

    private boolean isValidPhone(String phoneNumber){
        phoneNumber = PhoneNumberUtils.stripSeparators(phoneNumber);
        Matcher matcher = Pattern.compile("(\\+7)([0-9]){10}").matcher(phoneNumber);
        return matcher.matches();
    }

    public void onDestroy(){
        phoneAuthBus.unsubscribe(this);
    }
}

