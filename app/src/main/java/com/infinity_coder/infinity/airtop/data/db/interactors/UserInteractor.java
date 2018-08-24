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
public class UserInteractor extends BaseInteractor {

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

    public void updateUsername(String uuid, String username){
        service.submit(() -> {
            UserDao userDao = App.getInstance().getDatabase().userDao();
            userDao.updateUsername(uuid, username);
        });
    }

    public void updateName(String uuid, String name){
        service.submit(() -> {
            UserDao userDao = App.getInstance().getDatabase().userDao();
            userDao.updateName(uuid, name);
        });
    }

    public void updateBio(String uuid, String bio){
        service.submit(() -> {
            UserDao userDao = App.getInstance().getDatabase().userDao();
            userDao.updateBio(uuid, bio);
        });
    }

}
