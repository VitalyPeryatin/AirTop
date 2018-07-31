package com.example.infinity.airtop.presentation.presenters;

import com.example.infinity.airtop.views.SettingsActivity;

public class SettingsPresenter implements Presenter<SettingsActivity> {

    private SettingsActivity activity;

    private static SettingsPresenter presenter;

    private SettingsPresenter(){}

    public static synchronized SettingsPresenter getInstance(){
        if(presenter == null) presenter = new SettingsPresenter();
        return presenter;
    }

    @Override
    public void attachActivity(SettingsActivity activity) {
        this.activity = activity;
    }

    @Override
    public void detachActivity() {
        activity = null;
    }
}
