package com.example.infinity.airtop.ui.settings.updaters.username;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.example.infinity.airtop.R;
import com.example.infinity.airtop.data.db.interactors.UpdateUserInteractor;
import com.example.infinity.airtop.data.network.request.UpdateUsernameRequest;
import com.example.infinity.airtop.App;
import com.example.infinity.airtop.data.network.response.updaters.UpdateUsernameResponse;
import com.example.infinity.airtop.di.components.DaggerSettingsUpdateComponent;
import com.example.infinity.airtop.di.components.SettingsUpdateComponent;
import com.example.infinity.airtop.ui.settings.SettingsBus;
import com.example.infinity.airtop.utils.ServerPostman;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class UsernameSettingsActivity extends MvpAppCompatActivity implements TextWatcher, UsernameSettingsView {

    private Unbinder unbinder;
    @BindView(R.id.etFirstName)
    public EditText editTextUsername;
    @BindView(R.id.tvAccessInfo)
    public TextView tvAccessInfo;

    private SettingsUpdateComponent daggerComponent;

    @InjectPresenter
    UsernameSettingsPresenter presenter;
    @ProvidePresenter
    UsernameSettingsPresenter providePresenter(){
        return daggerComponent.provideUsernamePresenter();
    }

    @Inject
    UpdateUserInteractor interactor;
    @Inject
    ServerPostman serverPostman;
    @Inject
    SettingsBus<UpdateUsernameResponse> updateBus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        daggerComponent = DaggerSettingsUpdateComponent.create();
        daggerComponent.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_username_settings);
        unbinder = ButterKnife.bind(this);
        presenter.onCreate();

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
        String uuid = App.getInstance().getCurrentUser().uuid;
        serverPostman.postRequest(new UpdateUsernameRequest(uuid, username, availableToUpdate));
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
