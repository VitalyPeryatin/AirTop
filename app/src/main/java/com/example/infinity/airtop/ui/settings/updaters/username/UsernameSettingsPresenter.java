package com.example.infinity.airtop.ui.settings.updaters.username;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.infinity.airtop.data.db.interactors.UpdateUserInteractor;
import com.example.infinity.airtop.data.network.response.updaters.UpdateUsernameResponse;
import com.example.infinity.airtop.ui.settings.OnSettingsListener;
import com.example.infinity.airtop.ui.settings.SettingsBus;

@InjectViewState
public class UsernameSettingsPresenter extends MvpPresenter<UsernameSettingsView> implements OnSettingsListener<UpdateUsernameResponse> {
    private String uuid, username;
    // This boolean mast be of string type for successful processing on the server
    private String isAvailableToChange = "false";

    private SettingsBus<UpdateUsernameResponse> usernameSettingsBus;
    private UpdateUserInteractor interactor;

    public UsernameSettingsPresenter(UpdateUserInteractor interactor, SettingsBus<UpdateUsernameResponse> usernameSettingsBus){
        this.interactor = interactor;
        this.usernameSettingsBus = usernameSettingsBus;
    }

    @Override
    public void onUpdateSettings(UpdateUsernameResponse response) {
        uuid = response.getUuid();
        username = response.getUsername();
        if(response.getResult().equals(UpdateUsernameResponse.RESULT_OK)) {
            getViewState().onUsernameFree();
            isAvailableToChange = "true";
        }
        else{
            getViewState().onUsernameIsTaken();
            isAvailableToChange = "false";
        }
    }

    public void onCreate(){
        usernameSettingsBus.subscribe(this);
    }

    public void onTextChanged(String text){
        isAvailableToChange = "false";
        if(text.length() == 0) {
            getViewState().onEmptyUsernameField();
        }
        else if(text.length() < 5) {
            getViewState().onSmallUsername();
        }
        else {
            getViewState().onSendUsername(text, isAvailableToChange);
        }
    }

    public void saveChanges(){
        if(isAvailableToChange.equals("true")){
            getViewState().onSendUsername(username, isAvailableToChange);
            interactor.updateUsername(uuid, username);
            getViewState().onUpdateUsername();
            isAvailableToChange = "false";
        }
    }

    public void onDestroy() {
        super.onDestroy();
        usernameSettingsBus.unsubscribe(this);
    }
}
