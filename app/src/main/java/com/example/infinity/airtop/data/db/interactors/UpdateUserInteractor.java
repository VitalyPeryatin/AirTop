package com.example.infinity.airtop.data.db.interactors;

import com.example.infinity.airtop.App;
import com.example.infinity.airtop.data.db.repositoryDao.UserDao;
import com.example.infinity.airtop.ui.chat.ChatPresenter;
import com.example.infinity.airtop.ui.usernameUpdater.UsernameUpdaterPresenter;

/**
 * Provides methods to {@link UsernameUpdaterPresenter} for access to database
 * @author infinity_coder
 * @version 1.0.3
 */
public class UpdateUserInteractor extends BaseIntearctor{

    public void updateUsername(String phone, String username){
        service.submit(() -> {
            UserDao userDao = App.getInstance().getDatabase().userDao();
            userDao.updateUsername(phone, username);
        });
    }
}
