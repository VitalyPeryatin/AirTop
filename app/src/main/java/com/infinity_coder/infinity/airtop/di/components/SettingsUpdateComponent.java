package com.infinity_coder.infinity.airtop.di.components;

import com.infinity_coder.infinity.airtop.di.modules.SettingsUpdateModule;
import com.infinity_coder.infinity.airtop.ui.settings.updaters.name.NameSettingsActivity;
import com.infinity_coder.infinity.airtop.ui.settings.updaters.name.NameSettingsPresenter;
import com.infinity_coder.infinity.airtop.ui.settings.updaters.username.UsernameSettingsActivity;
import com.infinity_coder.infinity.airtop.ui.settings.updaters.username.UsernameSettingsPresenter;

import dagger.Component;

@Component(modules = SettingsUpdateModule.class)
public interface SettingsUpdateComponent {
    void inject(UsernameSettingsActivity activity);
    void inject(NameSettingsActivity activity);
    UsernameSettingsPresenter provideUsernamePresenter();
    NameSettingsPresenter provideNamePresenter();
}
