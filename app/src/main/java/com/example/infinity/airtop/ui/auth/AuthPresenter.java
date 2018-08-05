package com.example.infinity.airtop.ui.auth;

import android.content.Context;
import android.content.Intent;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.infinity.airtop.data.db.interactors.ChatInteractor;
import com.example.infinity.airtop.data.db.model.User;
import com.example.infinity.airtop.data.network.UserRequest;
import com.example.infinity.airtop.service.ClientService;
import com.example.infinity.airtop.utils.JsonConverter;
import com.example.infinity.airtop.App;

@InjectViewState
public class AuthPresenter extends MvpPresenter<AuthView> implements OnAuthListener {

    private Context context;
    private ChatInteractor chatInteractor;

    public AuthPresenter(){
        chatInteractor = new ChatInteractor();
    }

    public void onCreate(Context context){
        this.context = context;
        App.getInstance().getResponseListeners().getAuthListener().subscribe(this);
    }

    public void auth(String phone){
        if(isValidPhone(phone)) {
            new Thread(()->{
                UserRequest user = new UserRequest(phone);
                user.setAction(UserRequest.ACTION_CREATE);
                // TODO Перед вставкой пользователя в БД проверить, что на сервере данные получены

                JsonConverter jsonConverter = new JsonConverter();
                String json = jsonConverter.toJson(user);
                Intent intent = new Intent(context, ClientService.class);
                intent.putExtra("request", json);
                context.startService(intent);
            }).start();
        }
    }

    public void successPhoneAuth(UserRequest userRequest){
        User user = new User(userRequest);
        chatInteractor.insertUser(user);
        App.getInstance().setCurrentUser(userRequest);
        getViewState().successAuth();
    }

    private boolean isValidPhone(String phone){
        if(phone.length() < 10) return false;
        return true;
    }

    @Override
    public void onPhoneAuth(UserRequest user) {
        successPhoneAuth(user);
    }

    public void onDestroy() {
        super.onDestroy();
        App.getInstance().getResponseListeners().getAuthListener().unsubscribe(this);
    }
}
