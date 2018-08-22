package com.infinity_coder.infinity.airtop.ui.settings.updaters.bio;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.infinity_coder.infinity.airtop.App;
import com.infinity_coder.infinity.airtop.R;
import com.infinity_coder.infinity.airtop.data.db.interactors.UpdateUserInteractor;
import com.infinity_coder.infinity.airtop.data.network.request.UpdateBioRequest;
import com.infinity_coder.infinity.airtop.data.network.request.UpdateNameRequest;
import com.infinity_coder.infinity.airtop.data.network.response.updaters.UpdateNameResponse;
import com.infinity_coder.infinity.airtop.di.components.DaggerSettingsUpdateComponent;
import com.infinity_coder.infinity.airtop.di.components.SettingsUpdateComponent;
import com.infinity_coder.infinity.airtop.service.client.ServerConnection;
import com.infinity_coder.infinity.airtop.ui.settings.SettingsBus;
import com.infinity_coder.infinity.airtop.utils.ServerPostman;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class BioSettingsActivity extends MvpAppCompatActivity implements BioSettingsView{

    @BindView(R.id.toolbar_bio_settings)
    Toolbar toolbar;
    @BindView(R.id.etBio)
    EditText etBio;

    @Inject
    UpdateUserInteractor interactor;
    @Inject
    ServerPostman serverPostman;
    @Inject
    SettingsBus<UpdateNameResponse> updateBus;

    private Unbinder unbinder;
    private String uuid;
    private SettingsUpdateComponent daggerComponent;

    @InjectPresenter
    BioSettingsPresenter presenter;
    @ProvidePresenter
    BioSettingsPresenter providePresenter(){
        return daggerComponent.provideBioPresenter();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        daggerComponent = DaggerSettingsUpdateComponent.create();
        daggerComponent.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bio_settings);
        unbinder = ButterKnife.bind(this);

        setToolbar();
        String bio = getIntent().getExtras().getString("bio", "");
        etBio.setText(bio);
        uuid = App.getInstance().getCurrentUser().uuid;

        presenter.onCreate();
    }

    private void setToolbar(){
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.arrow_back);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());
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
                sendBio();
                break;
        }
        return true;
    }

    private void sendBio(){
        String bio = etBio.getText().toString();

        if(bio.length() > 0) {
            UpdateBioRequest request = new UpdateBioRequest(uuid, bio);
            ServerConnection.getInstance().sendRequest(request.toJson());
        }
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onUpdateUser() {
        App.getInstance().updateCurrentUser();
        setResult(RESULT_OK);
        finish();
    }
}
