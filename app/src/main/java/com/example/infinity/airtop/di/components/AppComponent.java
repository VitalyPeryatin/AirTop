package com.example.infinity.airtop.di.components;

import com.example.infinity.airtop.App;
import com.example.infinity.airtop.di.modules.AppModule;
import com.example.infinity.airtop.di.modules.TestAppModule;

import dagger.Component;

@Component(modules = AppModule.class)
public interface AppComponent {
    void inject(App app);
}
