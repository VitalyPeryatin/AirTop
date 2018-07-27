package com.example.infinity.airtop.views;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.widget.Button;
import android.widget.EditText;

import com.example.infinity.airtop.R;
import com.example.infinity.airtop.models.User;
import com.example.infinity.airtop.views.App;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class UsernameSettingsActivity extends Activity {

    private Unbinder unbinder;
    @BindView(R.id.editTextUsername)
    public EditText editTextUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_username_settings);
        unbinder = ButterKnife.bind(this);
    }

    @OnClick(R.id.btnApplyUsername)
    public void applyUsername(){
        String username = editTextUsername.getText().toString();
        notifyServer(username);
        Intent intent = new Intent();
        intent.putExtra("usernameKey", username);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void notifyServer(String username){
        App app = App.getInstance();
        User user = app.getCurrentUser();
        user.username = username;
        App.getBackendClient().sendRequest(user);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

}
