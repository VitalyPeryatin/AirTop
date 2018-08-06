package com.example.infinity.airtop.ui.usernameUpdater;


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
import com.example.infinity.airtop.data.db.model.User;
import com.example.infinity.airtop.data.network.CheckingUsername;
import com.example.infinity.airtop.data.network.UserRequest;
import com.example.infinity.airtop.data.network.request.UpdateUsernameRequest;
import com.example.infinity.airtop.service.ClientService;
import com.example.infinity.airtop.utils.JsonConverter;
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
    }


    @OnClick(R.id.btnApplyUsername)
    public void applyUsername(){
        presenter.saveChanges();
    }

    @Override
    public void onUpdateUsername(){
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onSendUsername(String username, String availableToUpdate) {
        String phone = App.getInstance().getCurrentUser().phone;
        UpdateUsernameRequest request = new UpdateUsernameRequest(phone, username, availableToUpdate);

        JsonConverter jsonConverter = new JsonConverter();
        String json = jsonConverter.toJson(request);
        Intent intent = new Intent(this, ClientService.class);
        intent.putExtra("request", json);
        startService(intent);
    }

    @Override
    public void onUsernameFree() {
        tvAccessInfo.setVisibility(View.VISIBLE);
        tvAccessInfo.setText("Username доступен");
    }

    @Override
    public void onEmptyUsernameField() {
        tvAccessInfo.setVisibility(View.GONE);
    }

    @Override
    public void onSmallUsername() {
        tvAccessInfo.setVisibility(View.VISIBLE);
        tvAccessInfo.setText("Username должен иметь хотя бы 5 символов");
    }

    @Override
    public void onUsernameIsTaken() {
        tvAccessInfo.setVisibility(View.VISIBLE);
        tvAccessInfo.setText("Username уже занят другим пользователем");
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
