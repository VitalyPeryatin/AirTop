package com.infinity_coder.infinity.airtop.di.modules;


import com.infinity_coder.infinity.airtop.App;
import com.infinity_coder.infinity.airtop.data.db.interactors.UpdateUserInteractor;
import com.infinity_coder.infinity.airtop.data.network.response.updaters.UpdateNameResponse;
import com.infinity_coder.infinity.airtop.data.network.response.updaters.UpdateUsernameResponse;
import com.infinity_coder.infinity.airtop.ui.settings.SettingsBus;
import com.infinity_coder.infinity.airtop.ui.settings.updaters.name.NameSettingsPresenter;
import com.infinity_coder.infinity.airtop.ui.settings.updaters.username.UsernameSettingsPresenter;
import com.infinity_coder.infinity.airtop.utils.ServerPostman;

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
    UpdateUserInteractor getInteractor(){
        return new UpdateUserInteractor();
    }

    @Provides
    ServerPostman getServerPostman(){
        return new ServerPostman();
    }

    @Provides
    UsernameSettingsPresenter provideUsernamePresenter(UpdateUserInteractor interactor, SettingsBus<UpdateUsernameResponse> updateBus){
        return new UsernameSettingsPresenter(interactor, updateBus);
    }

    @Provides
    NameSettingsPresenter provideNamePresenter(UpdateUserInteractor interactor, SettingsBus<UpdateNameResponse> updateBus){
        return new NameSettingsPresenter(interactor, updateBus);
    }
}
