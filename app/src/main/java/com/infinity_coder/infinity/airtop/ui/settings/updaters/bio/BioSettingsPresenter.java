package com.infinity_coder.infinity.airtop.ui.settings.updaters.bio;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.infinity_coder.infinity.airtop.data.db.interactors.UserInteractor;
import com.infinity_coder.infinity.airtop.data.network.response.updaters.UpdateBioResponse;
import com.infinity_coder.infinity.airtop.ui.settings.OnSettingsListener;
import com.infinity_coder.infinity.airtop.ui.settings.SettingsBus;

@InjectViewState
public class BioSettingsPresenter extends MvpPresenter<BioSettingsView>
        implements OnSettingsListener<UpdateBioResponse> {

    private SettingsBus<UpdateBioResponse> settingsBus;
    private UserInteractor interactor;

    public BioSettingsPresenter(UserInteractor interactor, SettingsBus<UpdateBioResponse> settingsBus){
        this.interactor = interactor;
        this.settingsBus = settingsBus;
    }

    public void onCreate(){
        settingsBus.subscribe(this);
    }

    @Override
    public void onUpdateSettings(UpdateBioResponse response) {
        String uuid = response.getUuid();
        String bio = response.getBio();
        interactor.updateBio(uuid, bio);
        getViewState().onUpdateUser();
    }

    @Override
    public void onDestroy(){
        settingsBus.unsubscribe(this);
        super.onDestroy();
    }
}
