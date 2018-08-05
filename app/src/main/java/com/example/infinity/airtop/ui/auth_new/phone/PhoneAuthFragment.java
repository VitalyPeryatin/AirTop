package com.example.infinity.airtop.ui.auth_new.phone;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.infinity.airtop.App;
import com.example.infinity.airtop.R;
import com.example.infinity.airtop.data.db.interactors.ChatInteractor;
import com.example.infinity.airtop.data.db.model.User;
import com.example.infinity.airtop.data.network.UserRequest;
import com.example.infinity.airtop.data.prefs.auth.AuthPreference;
import com.example.infinity.airtop.service.ClientService;
import com.example.infinity.airtop.ui.auth.OnAuthListener;
import com.example.infinity.airtop.ui.auth_new.AuthActivity;
import com.example.infinity.airtop.utils.JsonConverter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PhoneAuthFragment extends Fragment implements OnAuthListener {

    @BindView(R.id.etAuthPhone)
    EditText editTextAuth;
    private AuthActivity parentActivity;
    private AuthPreference sPref;
    private ChatInteractor interactor;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getInstance().getResponseListeners().getAuthListener().subscribe(this);
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
    public void onPhoneAuth(UserRequest userRequest) {
        User user = new User(userRequest);
        interactor.insertUser(user);
        App.getInstance().setCurrentUser(userRequest);

        sPref.saveCurrentPhone(userRequest.phone);
        if(!userRequest.nickname.equals("")) sPref.saveHaveNickname(true);
        parentActivity.changeView();
    }

    public void sendPhone(String phone){
        if(isValidPhone(phone)) {
            new Thread(()->{
                UserRequest user = new UserRequest(phone);
                user.setAction(UserRequest.ACTION_CREATE);
                // TODO Перед вставкой пользователя в БД проверить, что на сервере данные получены

                JsonConverter jsonConverter = new JsonConverter();
                String json = jsonConverter.toJson(user);
                Intent intent = new Intent(parentActivity, ClientService.class);
                intent.putExtra("request", json);
                parentActivity.startService(intent);
            }).start();
        }
    }

    @OnClick(R.id.btnAuth)
    public void auth(){
        String phone = editTextAuth.getText().toString();
        sendPhone(phone);
    }

    private boolean isValidPhone(String phone){
        if(phone.length() < 10) return false;
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        App.getInstance().getResponseListeners().getAuthListener().unsubscribe(this);
    }
}
