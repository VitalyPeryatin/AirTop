package com.example.infinity.airtop.models.databases;

import android.util.Log;

import com.example.infinity.airtop.models.User;
import com.example.infinity.airtop.views.App;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DatabaseHandler {

    private static DatabaseHandler handler;
    private ExecutorService executor;

    public static final String
            GET_USER_BY_PHONE = "get user by phone";

    private DatabaseHandler(){
        executor = Executors.newSingleThreadExecutor();
    }

    public static DatabaseHandler getInstance(){
        if(handler == null) handler = new DatabaseHandler();
        return handler;
    }

    public void runByTag(String tag, String addInfo){
        Runnable runnable = null;
        switch (tag){
            case GET_USER_BY_PHONE:
                runnable = () -> {
                    App app = App.getInstance();
                    UserDao userDao = app.getDatabase().userDao();
                    User user = userDao.getByPhone(addInfo);
                    app.setCurrentUser(user);
                    Log.d("mLog2", "" + user.phone);
                };
                break;
        }
        assert runnable != null;
        executor.submit(runnable);
    }


}
