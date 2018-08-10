package com.example.infinity.airtop.di.components;

import com.example.infinity.airtop.di.modules.UsernameUpdateModule;
import com.example.infinity.airtop.ui.settings.updaters.username.UsernameUpdaterActivity;
import com.example.infinity.airtop.ui.settings.updaters.username.UsernameUpdaterPresenter;

import dagger.Component;

@Component(modules = UsernameUpdateModule.class)
public interface UsernameUpdateComponent {
    void inject(UsernameUpdaterActivity activity);
    UsernameUpdaterPresenter providePresenter();
}
