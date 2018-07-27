package com.example.infinity.airtop.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.infinity.airtop.R;
import com.example.infinity.airtop.models.User;
import com.example.infinity.airtop.presenters.SettingsPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SettingsActivity extends AppCompatActivity {

    @BindView(R.id.tvPhone)
    TextView tvPhone;
    @BindView(R.id.tvUsername)
    TextView tvUsername;

    private SettingsPresenter presenter;
    private Unbinder unbinder;
    private static final int
        PHONE_CODE = 1,
        USERNAME_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        unbinder = ButterKnife.bind(this);
        presenter = SettingsPresenter.getInstance();
        presenter.attachActivity(this);

        loadUserData();
    }

    private void loadUserData(){
        User user = App.getInstance().getCurrentUser();
        tvPhone.setText(user.phone);
        tvUsername.setText(user.username);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        presenter.detachActivity();
    }

    @OnClick({R.id.phone_settings, R.id.username_settings})
    void changeProfileData(View view){
        Intent intent;
        switch (view.getId()){
            case R.id.phone_settings:
                intent = new Intent(this, PhoneSettingsActivity.class);
                startActivityForResult(intent, PHONE_CODE);
                break;
            case R.id.username_settings:
                intent = new Intent(this, UsernameSettingsActivity.class);
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
                    assert data != null;
                    assert data.getExtras() != null;

                    String username = data.getExtras().getString("usernameKey", "None");
                    tvUsername.setText(username);
                    break;
            }
        }
    }
}
