package com.example.infinity.airtop.ui.settings.updaters.username;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.example.infinity.airtop.R;
import com.example.infinity.airtop.data.network.request.UpdateUsernameRequest;
import com.example.infinity.airtop.App;
import com.example.infinity.airtop.utils.serverWorker.ServerPostman;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class UsernameUpdaterActivity extends MvpAppCompatActivity implements TextWatcher, UsernameUpdaterView {

    private Unbinder unbinder;
    @BindView(R.id.editTextUsername)
    public EditText editTextUsername;
    @BindView(R.id.tvAccessInfo)
    public TextView tvAccessInfo;

    @InjectPresenter
    UsernameUpdaterPresenter presenter;

    private ServerPostman serverPostman;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_username_settings);
        unbinder = ButterKnife.bind(this);
        presenter.onCreate();

        serverPostman = new ServerPostman();
        editTextUsername.addTextChangedListener(this);
        tvAccessInfo.setVisibility(View.GONE);
    }


    @OnClick(R.id.btnApplyUsername)
    public void applyUsername(){
        presenter.saveChanges();
    }

    @Override
    public void onUpdateUsername(){
        App.getInstance().updateCurrentUser();
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onSendUsername(String username, String availableToUpdate) {
        String phone = App.getInstance().getCurrentUser().phone;
        UpdateUsernameRequest request = new UpdateUsernameRequest(phone, username, availableToUpdate);
        serverPostman.postRequest(request);
    }

    @Override
    public void onUsernameFree() {
        tvAccessInfo.setVisibility(View.VISIBLE);
        tvAccessInfo.setText(R.string.text_username_is_available);
    }

    @Override
    public void onEmptyUsernameField() {
        tvAccessInfo.setVisibility(View.GONE);
    }

    @Override
    public void onSmallUsername() {
        tvAccessInfo.setVisibility(View.VISIBLE);
        tvAccessInfo.setText(R.string.text_low_username);
    }

    @Override
    public void onUsernameIsTaken() {
        tvAccessInfo.setVisibility(View.VISIBLE);
        tvAccessInfo.setText(R.string.text_username_taken);
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
