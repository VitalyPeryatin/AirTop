package com.example.infinity.airtop.di.modules.usernameUpdate;

import com.example.infinity.airtop.ui.settings.updaters.username.UsernameUpdateBus;

import dagger.Module;
import dagger.Provides;

@Module(includes = MainUsernameUpdateModule.class)
public class TestUsernameUpdateModule {
    @Provides
    UsernameUpdateBus getUsernameUpdateBus(){
        return new UsernameUpdateBus();
    }
}
