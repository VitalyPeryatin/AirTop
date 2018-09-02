package com.infinity_coder.infinity.airtop.ui.settings.updaters.name;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.infinity_coder.infinity.airtop.App;
import com.infinity_coder.infinity.airtop.R;
import com.infinity_coder.infinity.airtop.data.db.interactors.UserInteractor;
import com.infinity_coder.infinity.airtop.data.network.request.UpdateNameRequest;
import com.infinity_coder.infinity.airtop.data.network.response.updaters.UpdateNameResponse;
import com.infinity_coder.infinity.airtop.di.components.DaggerSettingsUpdateComponent;
import com.infinity_coder.infinity.airtop.di.components.SettingsUpdateComponent;
import com.infinity_coder.infinity.airtop.service.client.ServerConnection;
import com.infinity_coder.infinity.airtop.ui.settings.SettingsBus;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class NameSettingsActivity extends MvpAppCompatActivity implements NameSettingsView{

    @BindView(R.id.etFirstName)
    EditText etFirstName;
    @BindView(R.id.etLastName)
    EditText etLastName;
    @BindView(R.id.toolbar_name_settings)
    Toolbar toolbar;

    @Inject
    UserInteractor interactor;
    @Inject
    SettingsBus<UpdateNameResponse> updateBus;

    @InjectPresenter
    NameSettingsPresenter presenter;
    @ProvidePresenter
    NameSettingsPresenter providePresenter(){
        return daggerComponent.provideNamePresenter();
    }

    private SettingsUpdateComponent daggerComponent;
    private Unbinder unbinder;
    private String uuid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        daggerComponent = DaggerSettingsUpdateComponent.create();
        daggerComponent.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_settings);
        unbinder = ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            String firstName = bundle.getString("firstName", "");
            String lastName = bundle.getString("lastName", "");
            etFirstName.setText(firstName);
            etLastName.setText(lastName);
        }

        setToolbar();
        presenter.onCreate();
        uuid = App.getInstance().getCurrentUser().uuid;
    }

    private void setToolbar(){
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.arrow_back);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        presenter.onDestroy();
        super.onDestroy();
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
                sendName();
                break;
        }
        return true;
    }

    private void sendName(){
        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        String fullName = (firstName + " " + lastName).trim();
        if(firstName.length() > 0) {
            UpdateNameRequest request = new UpdateNameRequest(uuid, fullName);
            ServerConnection.getInstance().sendRequest(request);
        }
    }

    @Override
    public void onUpdateUser() {
        setResult(RESULT_OK);
        finish();
    }
}
