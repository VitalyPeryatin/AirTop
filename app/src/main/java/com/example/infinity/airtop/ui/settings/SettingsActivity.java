package com.example.infinity.airtop.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.infinity.airtop.R;
import com.example.infinity.airtop.data.db.model.User;
import com.example.infinity.airtop.App;
import com.example.infinity.airtop.ui.settings.updaters.name.NameSettingsActivity;
import com.example.infinity.airtop.ui.settings.updaters.phone.PhoneUpdaterActivity;
import com.example.infinity.airtop.ui.settings.updaters.username.UsernameSettingsActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SettingsActivity extends AppCompatActivity {

    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvPhone)
    TextView tvPhone;
    @BindView(R.id.tvUsername)
    TextView tvUsername;
    @BindView(R.id.tvBio)
    TextView tvBio;

    private Unbinder unbinder;
    private static final int
        PHONE_CODE = 1,
        USERNAME_CODE = 2,
        NAME_CODE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        unbinder = ButterKnife.bind(this);

        loadUserData();
    }

    private void loadUserData(){
        User user = App.getInstance().getCurrentUser();
        tvPhone.setText(user.phone);
        tvUsername.setText(user.username);
        tvName.setText(user.nickname);
        tvBio.setText(user.bio);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @OnClick(R.id.phone_settings)
    void onChangePhone(){
        Intent intent = new Intent(this, PhoneUpdaterActivity.class);
        startActivityForResult(intent, PHONE_CODE);
    }

    @OnClick(R.id.username_settings)
    void onChangeUsername(){
        Intent intent = new Intent(this, UsernameSettingsActivity.class);
        startActivityForResult(intent, USERNAME_CODE);
    }

    @OnClick(R.id.name_settings)
    void onChangeName(){
        Intent intent = new Intent(this, NameSettingsActivity.class);
        startActivityForResult(intent, NAME_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            switch (requestCode){
                case PHONE_CODE:
                    break;
                case USERNAME_CODE:
                    loadUserData();
                    break;
            }
        }
    }
}
