package com.example.infinity.airtop.ui.auth.phone;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.infinity.airtop.App;
import com.example.infinity.airtop.R;
import com.example.infinity.airtop.data.db.interactors.ChatInteractor;
import com.example.infinity.airtop.data.db.model.User;
import com.example.infinity.airtop.data.network.response.PhoneAuthResponse;
import com.example.infinity.airtop.data.network.request.PhoneAuthRequest;
import com.example.infinity.airtop.data.prefs.auth.AuthPreference;
import com.example.infinity.airtop.ui.auth.AuthActivity;
import com.example.infinity.airtop.utils.serverWorker.ServerPostman;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PhoneAuthFragment extends Fragment implements OnPhoneAuthListener {

    @BindView(R.id.etAuthPhone)
    EditText editTextAuth;
    private AuthActivity parentActivity;
    private AuthPreference sPref;
    private ChatInteractor interactor;
    private String phone;
    private ServerPostman serverPostman;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getInstance().getResponseListeners().getPhoneAuthBus().subscribe(this);
        serverPostman = new ServerPostman();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_phone_auth, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        parentActivity = (AuthActivity) getActivity();
        sPref = new AuthPreference();
        interactor = new ChatInteractor();
    }

    @Override
    public void onPhoneAuth(PhoneAuthResponse response) {
        Log.d("mLog2", "" + response.getResult() + " " + response.getUser());

        if(response.getResult().equals("RESULT_OK")){
            sPref.saveCurrentPhone(phone);
        }
        else if (response.getResult().equals("RESULT_EXISTS")){
            User user = response.getUser();
            sPref.saveCurrentPhone(phone);
            sPref.saveHaveNickname(true);
            interactor.insertUser(user);
            App.getInstance().updateCurrentUser();
        }
        parentActivity.changeView();
    }

    public void sendPhone(String phone){
        if(isValidPhone(phone)) {
            PhoneAuthRequest request = new PhoneAuthRequest(phone);
            serverPostman.postRequest(request);
        }
        else{
            editTextAuth.setText("");
        }
    }

    @OnClick(R.id.btnAuth)
    public void auth(){
        phone = editTextAuth.getText().toString();
        sendPhone(phone);
    }

    private boolean isValidPhone(String phone){
        phone = PhoneNumberUtils.stripSeparators(phone);
        Matcher matcher = Pattern.compile("(\\+7)([0-9]){10}").matcher(phone);
        return matcher.matches();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        App.getInstance().getResponseListeners().getPhoneAuthBus().unsubscribe(this);
    }
}
