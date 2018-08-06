package com.example.infinity.airtop.data.db.interactors;

import com.example.infinity.airtop.App;
import com.example.infinity.airtop.data.db.model.User;
import com.example.infinity.airtop.data.db.repositoryDao.UserDao;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class UserInteractor extends BaseIntearctor{

    public User getUserByPhone(String phone){
        Future<User> future = service.submit(() -> {
            UserDao userDao = App.getInstance().getDatabase().userDao();
            return userDao.getByPhone(phone);
        });
        try {
            return future.get(1, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
            return null;
        }
    }

}
