package com.infinity_coder.infinity.airtop.ui.settings.updaters.username;


import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.infinity_coder.infinity.airtop.App;
import com.infinity_coder.infinity.airtop.R;
import com.infinity_coder.infinity.airtop.data.db.interactors.UserInteractor;
import com.infinity_coder.infinity.airtop.data.network.request.UpdateUsernameRequest;
import com.infinity_coder.infinity.airtop.data.network.response.updaters.UpdateUsernameResponse;
import com.infinity_coder.infinity.airtop.di.components.DaggerSettingsUpdateComponent;
import com.infinity_coder.infinity.airtop.di.components.SettingsUpdateComponent;
import com.infinity_coder.infinity.airtop.service.client.ServerConnection;
import com.infinity_coder.infinity.airtop.ui.settings.SettingsBus;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class UsernameSettingsActivity extends MvpAppCompatActivity implements TextWatcher, UsernameSettingsView {

    private Unbinder unbinder;
    @BindView(R.id.etFirstName)
    EditText editTextUsername;
    @BindView(R.id.tvAccessInfo)
    TextView tvAccessInfo;
    @BindView(R.id.toolbar_username_settings)
    Toolbar toolbar;

    private SettingsUpdateComponent daggerComponent;

    @InjectPresenter
    UsernameSettingsPresenter presenter;
    @ProvidePresenter
    UsernameSettingsPresenter providePresenter(){
        return daggerComponent.provideUsernamePresenter();
    }

    @Inject
    UserInteractor interactor;
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

        setToolbar();
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            String username = bundle.getString("username", "");
            editTextUsername.setText(username);
        }
        editTextUsername.addTextChangedListener(this);
        tvAccessInfo.setVisibility(View.GONE);
    }

    private void setToolbar(){
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.arrow_back);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());
    }

    @Override
    public void onUpdateUsername(){
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_apply, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_apply:
                presenter.saveChanges();
                break;
        }
        return true;
    }

    @Override
    public void onSendUsername(String username, String availableToUpdate) {
        String uuid = App.getInstance().getCurrentUser().uuid;
        UpdateUsernameRequest request = new UpdateUsernameRequest(uuid, username, availableToUpdate);
        ServerConnection.getInstance().sendRequest(request.toJson());
    }

    @Override
    public void onUsernameFree() {
        tvAccessInfo.setVisibility(View.VISIBLE);
        tvAccessInfo.setText(R.string.text_username_is_available);
    }

    @Override
    public void onInvalidUsername() {
        tvAccessInfo.setVisibility(View.VISIBLE);
        tvAccessInfo.setText(R.string.invalid_username);
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
