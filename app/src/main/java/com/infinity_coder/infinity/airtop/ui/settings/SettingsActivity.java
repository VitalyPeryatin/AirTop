package com.infinity_coder.infinity.airtop.ui.settings;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.infinity_coder.infinity.airtop.R;
import com.infinity_coder.infinity.airtop.data.db.model.User;
import com.infinity_coder.infinity.airtop.App;
import com.infinity_coder.infinity.airtop.ui.settings.updaters.bio.BioSettingsActivity;
import com.infinity_coder.infinity.airtop.ui.settings.updaters.name.NameSettingsActivity;
import com.infinity_coder.infinity.airtop.ui.settings.updaters.phone.PhoneSettingsActivity;
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
    private User user;

    private static final int
        PHONE_CODE = 1,
        USERNAME_CODE = 2,
        NAME_CODE = 3,
        BIO_CODE = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        unbinder = ButterKnife.bind(this);

        setToolbar();
        App.getInstance().getCurrentLiveUser().observe(this, this);
    }

    private void setToolbar(){
        setSupportActionBar(toolbar);
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
        Intent intent = new Intent(this, PhoneSettingsActivity.class);
        startActivityForResult(intent, PHONE_CODE);
    }

    @OnClick(R.id.username_settings)
    void onChangeUsername(){
        String username = user.username.replaceAll("@", "");

        Intent intent = new Intent(this, UsernameSettingsActivity.class);
        intent.putExtra("username", username);
        startActivityForResult(intent, USERNAME_CODE);
    }

    @OnClick(R.id.name_settings)
    void onChangeName(){
        String nickname = user.nickname;
        String[] namesArray = nickname.split(" ", 2);
        String firstName = "";
        String lastName = "";

        if(namesArray.length == 2) {
            firstName = namesArray[0];
            lastName = namesArray[1];
        }
        else if(namesArray.length == 1){
            firstName = namesArray[0];
        }
        Intent intent = new Intent(this, NameSettingsActivity.class);
        intent.putExtra("firstName", firstName);
        intent.putExtra("lastName", lastName);
        startActivityForResult(intent, NAME_CODE);
    }

    @OnClick(R.id.bio_settings)
    void onChangeBio(){
        String bio = user.bio;

        Intent intent = new Intent(this, BioSettingsActivity.class);
        intent.putExtra("bio", bio);
        startActivityForResult(intent, BIO_CODE);
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
    public void onChanged(User user) {
        this.user = user;

        toolbar.setTitle(format(user.nickname));
        tvPhone.setText(format(user.phone));
        tvUsername.setText(format(user.username));
        tvName.setText(format(user.nickname));
        tvBio.setText(format(user.bio));
    }

    private String format(String text){
        if(text == null) return "None";
        return text.length() != 0 ? text : "None";
    }
}
