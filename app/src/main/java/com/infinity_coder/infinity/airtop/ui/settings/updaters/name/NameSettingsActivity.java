package com.infinity_coder.infinity.airtop.ui.settings.updaters.name;

import android.os.Bundle;
import android.widget.EditText;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.infinity_coder.infinity.airtop.App;
import com.infinity_coder.infinity.airtop.R;
import com.infinity_coder.infinity.airtop.data.db.interactors.UpdateUserInteractor;
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
import butterknife.OnClick;
import butterknife.Unbinder;

public class NameSettingsActivity extends MvpAppCompatActivity implements NameSettingsView{

    @BindView(R.id.etFirstName)
    EditText etFirstName;
    @BindView(R.id.etLastName)
    EditText etLastName;

    @Inject
    UpdateUserInteractor interactor;
    @Inject
    ServerPostman serverPostman;
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

        presenter.onCreate();
        unbinder = ButterKnife.bind(this);
        uuid = App.getInstance().getCurrentUser().uuid;
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        presenter.onDestroy();
        super.onDestroy();
    }

    @OnClick(R.id.btnChangeName)
    public void sendName(){
        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        String fullName = (firstName + " " + lastName).trim();
        if(firstName.length() > 0) {
            UpdateNameRequest request = new UpdateNameRequest(uuid, fullName);
            ServerConnection.getInstance().sendRequest(request.toJson());
        }
    }

    @Override
    public void onUpdateUser() {
        App.getInstance().updateCurrentUser();
        setResult(RESULT_OK);
        finish();
    }
}
