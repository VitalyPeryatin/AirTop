package com.example.infinity.airtop.ui.auth;

import android.content.Context;
import android.content.Intent;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.infinity.airtop.data.db.model.User;
import com.example.infinity.airtop.data.network.UserRequest;
import com.example.infinity.airtop.data.db.repositoryDao.UserDao;
import com.example.infinity.airtop.presentation.presenters.listeners.OnAuthListener;
import com.example.infinity.airtop.service.ClientService;
import com.example.infinity.airtop.service.client.JsonConverter;
import com.example.infinity.airtop.App;

@InjectViewState
public class LoginPresenter extends MvpPresenter<LoginView> implements OnAuthListener {

    private Context context;

    public LoginPresenter(){

    }

    public void onCreate(Context context){
        this.context = context;
        App.getInstance().getListeners().getAuthListener().subscribe(this);
    }

    public void auth(String phone){
        if(isValidPhone(phone)) {
            new Thread(()->{
                UserRequest user = new UserRequest(phone);
                user.setAction(UserRequest.ACTION_CREATE);
                //App.getInstance().getDatabase().userDao().insert(user); // TODO Перед вставкой пользователя в БД проверить, что на сервере данные получены

                JsonConverter jsonConverter = new JsonConverter();
                String json = jsonConverter.toJson(user);
                Intent intent = new Intent(context, ClientService.class);
                intent.putExtra("request", json);
                context.startService(intent);
                //App.getInstance().getBackendClient().sendRequest(user);
            }).start();
        }
    }

    public void successAuth(UserRequest userRequest){
        UserDao userDao = App.getInstance().getDatabase().userDao();
        User user = new User(userRequest);
        userDao.insert(user);
        App.getInstance().setCurrentUserPhone(user.phone);
        getViewState().successAuth();
    }

    private boolean isValidPhone(String phone){
        if(phone.length() < 10) return false;
        return true;
    }

    @Override
    public void onAuth(UserRequest user) {
        successAuth(user);
    }

    public void onDestroy() {
        super.onDestroy();
        App.getInstance().getListeners().getAuthListener().unsubscribe(this);
    }
}
