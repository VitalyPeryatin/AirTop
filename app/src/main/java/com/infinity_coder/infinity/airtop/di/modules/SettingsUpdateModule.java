package com.infinity_coder.infinity.airtop.di.modules;


import com.infinity_coder.infinity.airtop.App;
import com.infinity_coder.infinity.airtop.data.db.interactors.UserInteractor;
import com.infinity_coder.infinity.airtop.data.network.response.updaters.UpdateBioResponse;
import com.infinity_coder.infinity.airtop.data.network.response.updaters.UpdateNameResponse;
import com.infinity_coder.infinity.airtop.data.network.response.updaters.UpdateUsernameResponse;
import com.infinity_coder.infinity.airtop.ui.settings.SettingsBus;
import com.infinity_coder.infinity.airtop.ui.settings.updaters.bio.BioSettingsPresenter;
import com.infinity_coder.infinity.airtop.ui.settings.updaters.name.NameSettingsPresenter;
import com.infinity_coder.infinity.airtop.ui.settings.updaters.username.UsernameSettingsPresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class SettingsUpdateModule {
    @Provides
    SettingsBus<UpdateUsernameResponse> getUsernameUpdateBus(){
        return App.getInstance().getResponseListeners().getUsernameSettingsBus();
    }

    @Provides
    SettingsBus<UpdateNameResponse> getNameUpdateBus(){
        return App.getInstance().getResponseListeners().getNameSettingsBus();
    }

    @Provides
    SettingsBus<UpdateBioResponse> getBioUpdateBus(){
        return App.getInstance().getResponseListeners().getBioSettingsBus();
    }

    @Provides
    UserInteractor getInteractor() {
        return new UserInteractor();
    }

    @Provides
    UsernameSettingsPresenter provideUsernamePresenter(UserInteractor interactor, SettingsBus<UpdateUsernameResponse> updateBus){
        return new UsernameSettingsPresenter(interactor, updateBus);
    }

    @Provides
    NameSettingsPresenter provideNamePresenter(UserInteractor interactor, SettingsBus<UpdateNameResponse> updateBus){
        return new NameSettingsPresenter(interactor, updateBus);
    }

    @Provides
    BioSettingsPresenter provideBioPresenter(UserInteractor interactor, SettingsBus<UpdateBioResponse> updateBus){
        return new BioSettingsPresenter(interactor, updateBus);
    }
}
