package com.example.infinity.airtop.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import com.example.infinity.airtop.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EntranceActivity extends AppCompatActivity {

    @BindView(R.id.etSecretCode)
    EditText editTextSecretCode;
    @BindView(R.id.btnSendCode)
    Button btnSendCode;

    private final String SECRET_CODE = "road12345";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrance);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btnSendCode)
    void click(){
        if(editTextSecretCode.getText().toString().equals(SECRET_CODE)) {
            startActivity(new Intent(this, ChatActivity.class));
            finish();
        }
    }
}
