package com.example.infinity.airtop.ui.settings.updaters.username;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.infinity.airtop.data.db.interactors.UpdateUserInteractor;
import com.example.infinity.airtop.data.network.response.UpdateUsernameResponse;

@InjectViewState
public class UsernameUpdaterPresenter extends MvpPresenter<UsernameUpdaterView> implements OnUsernameUpdateListener{
    private String phone, username;
    // This boolean mast be of string type for successful processing on the server
    private String isAvailableToChange = "false";

    private UsernameUpdateBus usernameUpdateBus;
    private UpdateUserInteractor interactor;

    public UsernameUpdaterPresenter(UpdateUserInteractor interactor, UsernameUpdateBus usernameUpdateBus){
        this.interactor = interactor;
        this.usernameUpdateBus = usernameUpdateBus;
    }

    @Override
    public void onUpdateUsername(UpdateUsernameResponse response){
        phone = response.getPhone();
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
        usernameUpdateBus.subscribe(this);
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
            interactor.updateUsername(phone, username);
            getViewState().onUpdateUsername();
            isAvailableToChange = "false";
        }
    }

    public void onDestroy() {
        super.onDestroy();
        usernameUpdateBus.unsubscribe(this);
    }
}
