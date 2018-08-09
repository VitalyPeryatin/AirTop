package com.example.infinity.airtop.di.components;

import com.example.infinity.airtop.di.modules.usernameUpdate.RealUsernameUpdateModule;
import com.example.infinity.airtop.ui.settings.updaters.username.UsernameUpdateBus;
import com.example.infinity.airtop.ui.settings.updaters.username.UsernameUpdaterPresenter;

import dagger.Component;

@Component(modules = RealUsernameUpdateModule.class)
public interface UsernameUpdateComponent {
    void inject(UsernameUpdaterPresenter chatPresenter);
    UsernameUpdateBus getUsernameUpdateBus();
}
