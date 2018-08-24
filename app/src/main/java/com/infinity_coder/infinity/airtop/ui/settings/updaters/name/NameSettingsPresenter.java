package com.infinity_coder.infinity.airtop.ui.settings.updaters.name;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.infinity_coder.infinity.airtop.data.db.interactors.UserInteractor;
import com.infinity_coder.infinity.airtop.data.network.response.updaters.UpdateNameResponse;
import com.infinity_coder.infinity.airtop.ui.settings.OnSettingsListener;
import com.infinity_coder.infinity.airtop.ui.settings.SettingsBus;

@InjectViewState
public class NameSettingsPresenter extends MvpPresenter<NameSettingsView>
        implements OnSettingsListener<UpdateNameResponse> {

    private SettingsBus<UpdateNameResponse> settingsBus;
    private UserInteractor interactor;

    public NameSettingsPresenter(UserInteractor interactor, SettingsBus<UpdateNameResponse> settingsBus){
        this.interactor = interactor;
        this.settingsBus = settingsBus;
    }

    public void onCreate(){
        settingsBus.subscribe(this);
    }

    @Override
    public void onUpdateSettings(UpdateNameResponse response) {
        String uuid = response.getUuid();
        String name = response.getName();
        interactor.updateName(uuid, name);
        getViewState().onUpdateUser();
    }

    @Override
    public void onDestroy() {
        settingsBus.unsubscribe(this);
        super.onDestroy();
    }
}
