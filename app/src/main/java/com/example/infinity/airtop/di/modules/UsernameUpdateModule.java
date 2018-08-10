package com.example.infinity.airtop.di.modules;


import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.example.infinity.airtop.App;
import com.example.infinity.airtop.data.db.interactors.UpdateUserInteractor;
import com.example.infinity.airtop.ui.settings.updaters.username.UsernameUpdateBus;
import com.example.infinity.airtop.ui.settings.updaters.username.UsernameUpdaterPresenter;
import com.example.infinity.airtop.utils.ServerPostman;

import dagger.Module;
import dagger.Provides;

@Module
public class UsernameUpdateModule {
    @Provides
    UsernameUpdateBus getUsernameUpdateBus(){
        return App.getInstance().getResponseListeners().getUsernameUpdateBus();
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
    UsernameUpdaterPresenter providePresenter(UpdateUserInteractor interactor, UsernameUpdateBus updateBus){
        return new UsernameUpdaterPresenter(interactor, updateBus);
    }
}
