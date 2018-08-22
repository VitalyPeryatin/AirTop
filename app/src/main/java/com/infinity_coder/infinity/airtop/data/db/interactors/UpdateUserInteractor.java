package com.infinity_coder.infinity.airtop.data.db.interactors;

import com.infinity_coder.infinity.airtop.App;
import com.infinity_coder.infinity.airtop.data.db.repositoryDao.UserDao;
import com.infinity_coder.infinity.airtop.ui.settings.updaters.username.UsernameSettingsPresenter;

/**
 * Provides methods to {@link UsernameSettingsPresenter} for access to database
 * @author infinity_coder
 * @version 1.0.3
 */
public class UpdateUserInteractor extends BaseIntearctor{

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
