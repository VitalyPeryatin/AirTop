package com.example.infinity.airtop.ui.settings.updaters.name;

import android.os.Bundle;
import android.widget.EditText;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.example.infinity.airtop.R;
import com.example.infinity.airtop.data.db.interactors.UpdateUserInteractor;
import com.example.infinity.airtop.data.network.response.updaters.UpdateNameResponse;
import com.example.infinity.airtop.data.network.response.updaters.UpdateUsernameResponse;
import com.example.infinity.airtop.di.components.DaggerSettingsUpdateComponent;
import com.example.infinity.airtop.di.components.SettingsUpdateComponent;
import com.example.infinity.airtop.ui.settings.SettingsBus;
import com.example.infinity.airtop.utils.ServerPostman;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        daggerComponent = DaggerSettingsUpdateComponent.create();
        daggerComponent.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_settings);

        presenter.onCreate();
        unbinder = ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        presenter.onDestroy();
        super.onDestroy();
    }
}
