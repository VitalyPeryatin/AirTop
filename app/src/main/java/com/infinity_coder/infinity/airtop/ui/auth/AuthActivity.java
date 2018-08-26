package com.infinity_coder.infinity.airtop.ui.auth;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

import com.infinity_coder.infinity.airtop.R;
import com.infinity_coder.infinity.airtop.data.db.interactors.UserInteractor;
import com.infinity_coder.infinity.airtop.data.db.model.User;
import com.infinity_coder.infinity.airtop.data.network.request.VerifyUserRequest;
import com.infinity_coder.infinity.airtop.data.prefs.auth.AuthPreference;
import com.infinity_coder.infinity.airtop.service.client.ServerConnection;
import com.infinity_coder.infinity.airtop.ui.auth.entry_code.EntryAuthFragment;
import com.infinity_coder.infinity.airtop.ui.auth.nickname.NicknameAuthFragment;
import com.infinity_coder.infinity.airtop.ui.auth.phone.PhoneAuthFragment;

import java.util.ArrayList;

/**
 * Activity controls fragments for auth, after full auth verify user on server
 * @author infinity_coder
 * @version 1.0.5
 */
public class AuthActivity extends AppCompatActivity{

    private AuthPreference sPref;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        sPref = new AuthPreference(getBaseContext());

        changeView();
    }

    public void changeView(){
        if(!sPref.isEntered())
            replaceFragment(new EntryAuthFragment());
        else if(sPref.getCurrentPhone() == null)
            replaceFragment(new PhoneAuthFragment());
        else if(!sPref.haveNickname())
            replaceFragment(new NicknameAuthFragment());
        else {
            verifyUser();
            setResult(RESULT_OK);
            finish();
        }
    }

    private void verifyUser(){
        UserInteractor interactor  = new UserInteractor();
        ArrayList<User> users = interactor.getAllUsers();
        if(users != null && users.size() > 0) {
            for (User user : users) {
                VerifyUserRequest request = new VerifyUserRequest(user.uuid);
                ServerConnection.getInstance().sendRequest(request.toJson());
            }
        }
    }

    private void replaceFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

    public void addFragmentToBackstack(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
