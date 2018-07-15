package com.example.infinity.airtop.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.example.infinity.airtop.R;
import com.example.infinity.airtop.model.User;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.etAuthPhone)
    EditText editTextAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }


    @OnClick(R.id.btnAuth)
    void auth(){
        String phone = editTextAuth.getText().toString();
        if(isValidPhone(phone)) {
            User user = new User(phone);
            Gson gson = new Gson();
            String jsonUser = gson.toJson(user);

            Intent intent = new Intent();
            intent.putExtra("user", jsonUser);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    private boolean isValidPhone(String phone){
        if(phone.length() < 10) return false;
        return true;
    }
}
