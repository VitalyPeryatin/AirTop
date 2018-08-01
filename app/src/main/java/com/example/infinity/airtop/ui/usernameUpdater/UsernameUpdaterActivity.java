package com.example.infinity.airtop.ui.usernameUpdater;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.example.infinity.airtop.R;
import com.example.infinity.airtop.data.network.CheckingUsername;
import com.example.infinity.airtop.data.network.UserRequest;
import com.example.infinity.airtop.service.ClientService;
import com.example.infinity.airtop.service.client.JsonConverter;
import com.example.infinity.airtop.App;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.example.infinity.airtop.data.network.CheckingUsername.RESULT_CANCEL;
import static com.example.infinity.airtop.data.network.CheckingUsername.RESULT_EMPTY;
import static com.example.infinity.airtop.data.network.CheckingUsername.RESULT_LITTLE;

public class UsernameUpdaterActivity extends MvpAppCompatActivity implements TextWatcher, UsernameUpdaterView {

    private Unbinder unbinder;
    @BindView(R.id.editTextUsername)
    public EditText editTextUsername;
    @BindView(R.id.tvAccessInfo)
    public TextView tvAccessInfo;
    private CheckingUsername checkingUsername;

    @InjectPresenter
    UsernameUpdaterPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_username_settings);
        unbinder = ButterKnife.bind(this);
        presenter.onCreate(this);

        editTextUsername.addTextChangedListener(this);
        tvAccessInfo.setVisibility(View.GONE);

        checkingUsername = new CheckingUsername();
        checkingUsername.setResult(RESULT_EMPTY);
    }


    @OnClick(R.id.btnApplyUsername)
    public void applyUsername(){
        if(checkingUsername.getResult().equals(CheckingUsername.RESULT_OK)) {
            String username = editTextUsername.getText().toString();
            notifyServer(username);
        }
    }

    @Override
    public void onUpdateUsername(){
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onResultUsernameCheck(CheckingUsername checkingUsername){
        runOnUiThread(()->{
            this.checkingUsername = checkingUsername;
            if(checkingUsername.getResult().equals(RESULT_EMPTY)){
                tvAccessInfo.setVisibility(View.GONE);
            }
            else{
                tvAccessInfo.setVisibility(View.VISIBLE);
                if(checkingUsername.getResult().equals(RESULT_LITTLE)){
                    tvAccessInfo.setText("Username должен иметь хотя бы 5 символов");
                }
                else if(checkingUsername.getResult().equals(RESULT_CANCEL)){
                    tvAccessInfo.setText("Username уже занят другим пользователем");
                }
                else if(checkingUsername.getResult().equals(CheckingUsername.RESULT_OK)){
                    tvAccessInfo.setText("Username доступен");
                }
            }
            Toast.makeText(this, checkingUsername.getResult(), Toast.LENGTH_SHORT).show();
        });
    }

    private void notifyServer(String username){
        App app = App.getInstance();
        UserRequest user = app.getCurrentUser();
        user.username = username;
        user.setAction(UserRequest.ACTION_UPDATE);

        JsonConverter jsonConverter = new JsonConverter();
        String json = jsonConverter.toJson(user);
        Intent intent = new Intent(this, ClientService.class);
        intent.putExtra("request", json);
        startService(intent);
        //App.getInstance().getBackendClient().sendRequest(user);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        presenter.onDestroy();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        presenter.onTextChanged(String.valueOf(charSequence));
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
