package com.example.infinity.airtop.ui.auth;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.infinity.airtop.R;
import com.example.infinity.airtop.data.db.interactors.UserInteractor;
import com.example.infinity.airtop.data.db.model.User;
import com.example.infinity.airtop.data.network.request.VerifyUserRequest;
import com.example.infinity.airtop.data.prefs.auth.AuthPreference;
import com.example.infinity.airtop.ui.auth.entry_code.EntryAuthFragment;
import com.example.infinity.airtop.ui.auth.nickname.NicknameAuthFragment;
import com.example.infinity.airtop.ui.auth.phone.PhoneAuthFragment;
import com.example.infinity.airtop.utils.ServerPostman;

import java.util.ArrayList;

/**
 * Activity controls fragments for auth, after full auth verify user on server
 * @author infinity_coder
 * @version 1.0.4
 */
public class AuthActivity extends AppCompatActivity{

    private AuthPreference sPref;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_auth);
        sPref = new AuthPreference();

        changeView();
    }

    public void changeView(){
        if(!sPref.isEntered())
            setFragment(new EntryAuthFragment());
        else if(sPref.getCurrentPhone() == null)
            setFragment(new PhoneAuthFragment());
        else if(!sPref.haveNickname())
            setFragment(new NicknameAuthFragment());
        else {
            verifyUser();
            setResult(RESULT_OK);
            finish();
        }
    }

    private void verifyUser(){
        UserInteractor interactor  = new UserInteractor();
        ArrayList<User> users = interactor.getAllUsers();
        if(users.size() > 0) {
            for (User user : users) {
                VerifyUserRequest request = new VerifyUserRequest(user.uuid);
                new ServerPostman().postRequest(request);
            }
        }
    }

    private void setFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }
}
