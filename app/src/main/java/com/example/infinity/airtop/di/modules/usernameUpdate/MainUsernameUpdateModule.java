package com.example.infinity.airtop.di.modules.usernameUpdate;

import com.example.infinity.airtop.data.db.interactors.UpdateUserInteractor;

import dagger.Module;
import dagger.Provides;

@Module
public class MainUsernameUpdateModule {
    @Provides
    UpdateUserInteractor getInteractor(){
        return new UpdateUserInteractor();
    }
}
