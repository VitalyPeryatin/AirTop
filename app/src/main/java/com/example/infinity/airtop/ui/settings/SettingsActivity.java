package com.example.infinity.airtop.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.infinity.airtop.R;
import com.example.infinity.airtop.data.network.UserRequest;
import com.example.infinity.airtop.App;
import com.example.infinity.airtop.ui.phoneUpdater.PhoneUpdaterActivity;
import com.example.infinity.airtop.ui.usernameUpdater.UsernameUpdaterActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SettingsActivity extends AppCompatActivity {

    @BindView(R.id.tvUsername)
    TextView tvPhone;
    @BindView(R.id.tvNickname)
    TextView tvUsername;

    private Unbinder unbinder;
    private static final int
        PHONE_CODE = 1,
        USERNAME_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        unbinder = ButterKnife.bind(this);

        loadUserData();
    }

    private void loadUserData(){
        UserRequest user = App.getInstance().getCurrentUser();
        tvPhone.setText(user.phone);
        tvUsername.setText(user.username);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @OnClick({R.id.phone_settings, R.id.username_settings})
    void changeProfileData(View view){
        Intent intent;
        switch (view.getId()){
            case R.id.phone_settings:
                intent = new Intent(this, PhoneUpdaterActivity.class);
                startActivityForResult(intent, PHONE_CODE);
                break;
            case R.id.username_settings:
                intent = new Intent(this, UsernameUpdaterActivity.class);
                startActivityForResult(intent, USERNAME_CODE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            switch (requestCode){
                case PHONE_CODE:
                    break;
                case USERNAME_CODE:
                    String username = App.getInstance().getCurrentUser().username;
                    tvUsername.setText(username);
                    break;
            }
        }
    }
}
