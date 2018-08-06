package com.example.infinity.airtop.ui.auth;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.infinity.airtop.R;
import com.example.infinity.airtop.data.prefs.auth.AuthPreference;
import com.example.infinity.airtop.ui.auth.entry_code.EntryAuthFragment;
import com.example.infinity.airtop.ui.auth.nickname.NicknameAuthFragment;
import com.example.infinity.airtop.ui.auth.phone.PhoneAuthFragment;

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
            setResult(RESULT_OK);
            finish();
        }
    }

    private void setFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }
}
