package com.example.infinity.airtop.presentation.presenters;

import com.example.infinity.airtop.models.CheckingUsername;
import com.example.infinity.airtop.models.User;
import com.example.infinity.airtop.models.databases.UserDao;
import com.example.infinity.airtop.views.App;
import com.example.infinity.airtop.views.UsernameSettingsActivity;

import static com.example.infinity.airtop.models.CheckingUsername.RESULT_EMPTY;
import static com.example.infinity.airtop.models.CheckingUsername.RESULT_LITTLE;

public class UsernameSettingsPresenter implements Presenter<UsernameSettingsActivity>{

    private static UsernameSettingsPresenter presenter;
    private UsernameSettingsActivity activity;

    public static UsernameSettingsPresenter getInstance() {
        if(presenter == null) presenter = new UsernameSettingsPresenter();
        return presenter;
    }

    @Override
    public void attachActivity(UsernameSettingsActivity activity) {
        this.activity = activity;
    }

    public void onUpdateUsername(User user){
        UserDao userDao = App.getInstance().getDatabase().userDao();
        userDao.insert(user);
        App.getInstance().setCurrentUser(user);
        activity.onUpdateUsername();
    }

    public void onResultUsernameCheck(CheckingUsername checkingUsername){
        activity.runOnUiThread(()->activity.onResultUsernameCheck(checkingUsername));
    }

    @Override
    public void detachActivity() {
        this.activity = null;
    }

    public void onTextChanged(String text){
        CheckingUsername checkingUsername = new CheckingUsername();
        if(text.length() == 0) {
            checkingUsername.setResult(RESULT_EMPTY);
            activity.onResultUsernameCheck(checkingUsername);
        }
        else if(text.length() < 5) {
            checkingUsername.setResult(RESULT_LITTLE);
            activity.onResultUsernameCheck(checkingUsername);
        }
        else {
            App.getInstance().getBackendClient().sendRequest(new CheckingUsername(text));
        }
    }
}
