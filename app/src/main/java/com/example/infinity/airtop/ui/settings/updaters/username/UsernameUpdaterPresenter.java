package com.example.infinity.airtop.ui.settings.updaters.username;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.example.infinity.airtop.data.db.interactors.UpdateUserInteractor;
import com.example.infinity.airtop.data.network.response.UpdateUsernameResponse;
import com.example.infinity.airtop.App;
import com.example.infinity.airtop.di.components.DaggerChatComponent;
import com.example.infinity.airtop.di.components.DaggerUsernameUpdateComponent;
import com.example.infinity.airtop.di.components.UsernameUpdateComponent;

import javax.inject.Inject;

@InjectViewState
public class UsernameUpdaterPresenter extends MvpPresenter<UsernameUpdaterView> implements OnUsernameUpdateListener{
    private String phone, username;
    // This boolean mast be of string type for successful processing on the server
    private String isAvailableToChange = "false";

    @Inject
    public UsernameUpdateBus usernameUpdateBus;
    @Inject
    public UpdateUserInteractor interactor;

    UsernameUpdaterPresenter(){
        interactor = new UpdateUserInteractor();
        UsernameUpdateComponent component = DaggerUsernameUpdateComponent.create();
        component.inject(this);
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
