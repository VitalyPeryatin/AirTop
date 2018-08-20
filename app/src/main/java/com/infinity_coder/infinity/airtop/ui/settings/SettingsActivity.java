package com.infinity_coder.infinity.airtop.ui.settings;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.infinity_coder.infinity.airtop.R;
import com.infinity_coder.infinity.airtop.data.db.model.User;
import com.infinity_coder.infinity.airtop.App;
import com.infinity_coder.infinity.airtop.ui.settings.updaters.name.NameSettingsActivity;
import com.infinity_coder.infinity.airtop.ui.settings.updaters.phone.PhoneUpdaterActivity;
import com.infinity_coder.infinity.airtop.ui.settings.updaters.username.UsernameSettingsActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SettingsActivity extends AppCompatActivity implements Observer<User>{

    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvPhone)
    TextView tvPhone;
    @BindView(R.id.tvUsername)
    TextView tvUsername;
    @BindView(R.id.tvBio)
    TextView tvBio;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private Unbinder unbinder;
    private LiveData<User> userLiveData;
    private static final int
        PHONE_CODE = 1,
        USERNAME_CODE = 2,
        NAME_CODE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        unbinder = ButterKnife.bind(this);


        setToolbar();
        App.getInstance().getCurrentLiveUser().observe(this, this);
    }

    private void setToolbar() {
        toolbar.setNavigationIcon(R.drawable.arrow_back);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());
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
                case PHONE_CODE | USERNAME_CODE:
                    break;
            }
        }
    }

    @Override
    public void onChanged(@Nullable User user) {
        toolbar.setTitle(user.nickname);
        tvPhone.setText(user.phone);
        tvUsername.setText(user.username);
        tvName.setText(user.nickname);
        tvBio.setText(user.bio);
    }
}
