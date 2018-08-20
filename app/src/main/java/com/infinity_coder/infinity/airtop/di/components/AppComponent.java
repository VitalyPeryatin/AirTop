package com.infinity_coder.infinity.airtop.di.components;

import com.infinity_coder.infinity.airtop.App;
import com.infinity_coder.infinity.airtop.di.modules.AppModule;

import dagger.Component;

@Component(modules = AppModule.class)
public interface AppComponent {
    void inject(App app);
}
