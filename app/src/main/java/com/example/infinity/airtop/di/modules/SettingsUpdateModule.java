package com.example.infinity.airtop.di.modules;


import com.example.infinity.airtop.App;
import com.example.infinity.airtop.data.db.interactors.UpdateUserInteractor;
import com.example.infinity.airtop.data.network.response.updaters.UpdateNameResponse;
import com.example.infinity.airtop.data.network.response.updaters.UpdateUsernameResponse;
import com.example.infinity.airtop.ui.settings.SettingsBus;
import com.example.infinity.airtop.ui.settings.updaters.name.NameSettingsPresenter;
import com.example.infinity.airtop.ui.settings.updaters.username.UsernameSettingsPresenter;
import com.example.infinity.airtop.utils.ServerPostman;

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
