package com.example.infinity.airtop.ui.auth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.EditText;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.example.infinity.airtop.App;
import com.example.infinity.airtop.R;
import com.example.infinity.airtop.ui.entrance.EntranceActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends MvpAppCompatActivity implements LoginView{

    @InjectPresenter
    LoginPresenter loginPresenter;
    private static final int ACCESS_CODE = 1;
    @BindView(R.id.etAuthPhone)
    EditText editTextAuth;
    String phone = null;

    private static final String KEY_ENTER = "isEntered";
    private SharedPreferences sPref;
    private boolean isEntered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        loginPresenter.onCreate(this);


        sPref = getSharedPreferences("savedEntryCode", MODE_PRIVATE);

        isEntered = sPref.getBoolean(KEY_ENTER, false);
        if (!isEntered) {
            startActivityForResult(new Intent(this, EntranceActivity.class), ACCESS_CODE);
        }
    }


    @OnClick(R.id.btnAuth)
    void auth(){
        phone = editTextAuth.getText().toString();
        loginPresenter.auth(phone);
    }

    @Override
    public void successAuth(){
        Intent data = new Intent();
        data.putExtra("user phone", phone);
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveEntranceState();
        loginPresenter.onDestroy();
    }



    // Save value of "isEntered"
    private void saveEntranceState(){
        SharedPreferences.Editor editor = sPref.edit();
        editor.putBoolean(KEY_ENTER, isEntered);
        editor.apply();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == ACCESS_CODE)
            isEntered = true;
    }
}
