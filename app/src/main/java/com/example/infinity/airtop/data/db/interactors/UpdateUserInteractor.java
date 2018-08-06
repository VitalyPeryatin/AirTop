package com.example.infinity.airtop.data.db.interactors;

import com.example.infinity.airtop.App;
import com.example.infinity.airtop.data.db.repositoryDao.UserDao;

public class UpdateUserInteractor extends BaseIntearctor{

    public void updateUsername(String phone, String username){
        service.submit(() -> {
            UserDao userDao = App.getInstance().getDatabase().userDao();
            userDao.updateUsername(phone, username);
        });
    }
}
