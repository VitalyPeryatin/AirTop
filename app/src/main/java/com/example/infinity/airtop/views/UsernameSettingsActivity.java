package com.example.infinity.airtop.views;


import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.infinity.airtop.R;
import com.example.infinity.airtop.models.CheckingUsername;
import com.example.infinity.airtop.models.User;
import com.example.infinity.airtop.presentation.presenters.UsernameSettingsPresenter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.example.infinity.airtop.models.CheckingUsername.RESULT_CANCEL;
import static com.example.infinity.airtop.models.CheckingUsername.RESULT_EMPTY;
import static com.example.infinity.airtop.models.CheckingUsername.RESULT_LITTLE;

public class UsernameSettingsActivity extends Activity implements TextWatcher {

    private Unbinder unbinder;
    @BindView(R.id.editTextUsername)
    public EditText editTextUsername;
    @BindView(R.id.tvAccessInfo)
    public TextView tvAccessInfo;
    private CheckingUsername checkingUsername;

    private UsernameSettingsPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_username_settings);
        unbinder = ButterKnife.bind(this);

        presenter = UsernameSettingsPresenter.getInstance();
        presenter.attachActivity(this);

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

    public void onUpdateUsername(){
        setResult(RESULT_OK);
        finish();
    }

    public void onResultUsernameCheck(CheckingUsername checkingUsername){
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
    }

    private void notifyServer(String username){
        App app = App.getInstance();
        User user = app.getCurrentUser();
        user.username = username;
        user.setAction(User.ACTION_UPDATE);
        App.getInstance().getBackendClient().sendRequest(user);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        presenter.detachActivity();
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
