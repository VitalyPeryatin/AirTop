package com.example.infinity.airtop.data.db.interactors;

import com.example.infinity.airtop.App;
import com.example.infinity.airtop.data.db.model.User;
import com.example.infinity.airtop.data.db.repositoryDao.UserDao;
import com.example.infinity.airtop.ui.main.MainActivity;

import java.util.ArrayList;
import java.util.List;
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
