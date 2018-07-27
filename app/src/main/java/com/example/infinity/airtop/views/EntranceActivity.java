package com.example.infinity.airtop.views;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Toast;

import com.example.infinity.airtop.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EntranceActivity extends AppCompatActivity {

    @BindView(R.id.etSecretCode)
    EditText editTextSecretCode;

    // Show when user already have written entry-code
    private static final String SECRET_CODE = "road12345";
    private Toast toastEmptyField;

    @SuppressLint("ShowToast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrance);
        ButterKnife.bind(this);

        toastEmptyField = Toast.makeText(this, "Введите код для начала авторизации", Toast.LENGTH_SHORT);
    }

    @OnClick(R.id.btnSendCode)
    void click(){
        String code = editTextSecretCode.getText().toString();
        if(code.length() > 0) {
            if (code.equals(SECRET_CODE)) {
                setResult(RESULT_OK);
                finish();
            } else {
                editTextSecretCode.setText("");
            }
        }
        else{
            toastEmptyField.cancel();
            toastEmptyField.show();
        }
    }


}
