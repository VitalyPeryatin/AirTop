package com.infinity_coder.infinity.airtop.data.db.interactors;

import android.arch.lifecycle.LiveData;

import com.infinity_coder.infinity.airtop.App;
import com.infinity_coder.infinity.airtop.data.db.model.User;
import com.infinity_coder.infinity.airtop.data.db.repositoryDao.UserDao;
import com.infinity_coder.infinity.airtop.ui.main.MainActivity;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Provides methods to {@link App} and {@link MainActivity} for access to database
 * @author infinity_coder
 * @version 1.0.3
 */
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

    public LiveData<User> getLiveUserByPhone(String phone){
        Future<LiveData<User>> future = service.submit(() -> {
            UserDao userDao = App.getInstance().getDatabase().userDao();
            return userDao.getUserByPhone(phone);
        });
        try {
            return future.get(10, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<User> getAllUsers(){
        Future<ArrayList<User>> future = service.submit(() -> {
            UserDao userDao = App.getInstance().getDatabase().userDao();
            return (ArrayList<User>)userDao.getAll();
        });
        try {
            return future.get(1, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
            return null;
        }
    }

}
