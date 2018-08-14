package com.example.infinity.airtop.ui.settings.updaters.name;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.infinity.airtop.data.db.interactors.UpdateUserInteractor;
import com.example.infinity.airtop.data.network.response.updaters.UpdateNameResponse;
import com.example.infinity.airtop.data.network.response.updaters.UpdateUsernameResponse;
import com.example.infinity.airtop.ui.settings.OnSettingsListener;
import com.example.infinity.airtop.ui.settings.SettingsBus;

@InjectViewState
public class NameSettingsPresenter extends MvpPresenter<NameSettingsView>
        implements OnSettingsListener<UpdateNameResponse>{

    private SettingsBus<UpdateNameResponse> settingsBus;
    private UpdateUserInteractor interactor;

    public NameSettingsPresenter(UpdateUserInteractor interactor, SettingsBus<UpdateNameResponse> settingsBus){
        this.interactor = interactor;
        this.settingsBus = settingsBus;
    }

    public void onCreate(){
        settingsBus.subscribe(this);
    }

    @Override
    public void onUpdateSettings(UpdateNameResponse response) {

    }

    @Override
    public void onDestroy() {
        settingsBus.unsubscribe(this);
        super.onDestroy();
    }
}
