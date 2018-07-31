package com.example.infinity.airtop.presentation.presenters;

import com.example.infinity.airtop.models.User;
import com.example.infinity.airtop.models.databases.UserDao;
import com.example.infinity.airtop.views.App;
import com.example.infinity.airtop.views.LoginActivity;

public class LoginPresenter implements Presenter<LoginActivity> {

    private static LoginPresenter loginPresenter;
    private LoginActivity activity;

    private LoginPresenter(){}

    public static LoginPresenter getInstance(){
        if(loginPresenter == null) loginPresenter = new LoginPresenter();
        return loginPresenter;
    }

    @Override
    public void attachActivity(LoginActivity activity) {
        this.activity = activity;
    }

    @Override
    public void detachActivity() {
        activity = null;
    }

    public void auth(String phone){
        if(isValidPhone(phone)) {
            new Thread(()->{
                User user = new User(phone);
                user.setAction(User.ACTION_CREATE);
                //App.getInstance().getDatabase().userDao().insert(user); // TODO Перед вставкой пользователя в БД проверить, что на сервере данные получены
                App.getInstance().getBackendClient().sendRequest(user);
            }).start();
        }
    }

    public void successAuth(User user){
        UserDao userDao = App.getInstance().getDatabase().userDao();
        userDao.insert(user);
        App.getInstance().setCurrentUser(user);
        activity.successAuth();
    }

    private boolean isValidPhone(String phone){
        if(phone.length() < 10) return false;
        return true;
    }
}
