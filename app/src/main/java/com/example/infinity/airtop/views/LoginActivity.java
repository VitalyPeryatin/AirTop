package com.example.infinity.airtop.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;

import com.example.infinity.airtop.R;
import com.example.infinity.airtop.models.User;
import com.example.infinity.airtop.presenters.LoginPresenter;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    private static final int ACCESS_CODE = 1;
    @BindView(R.id.etAuthPhone)
    EditText editTextAuth;
    LoginPresenter loginPresenter;
    String phone = null;

    private static final String KEY_ENTER = "isEntered";
    private SharedPreferences sPref;
    private boolean isEntered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        loginPresenter = LoginPresenter.getInstance();
        loginPresenter.attachActivity(this);

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
