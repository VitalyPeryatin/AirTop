package com.example.infinity.airtop.ui.usernameUpdater;

import android.content.Context;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.infinity.airtop.data.db.interactors.UpdateUserInteractor;
import com.example.infinity.airtop.data.network.response.UpdateUsernameResponse;
import com.example.infinity.airtop.App;

@InjectViewState
public class UsernameUpdaterPresenter extends MvpPresenter<UsernameUpdaterView> implements OnUsernameUpdateListener{
    private Context context;
    private UpdateUserInteractor interactor;
    private String phone, username;
    private String isAvailableToChange = "false";

    public UsernameUpdaterPresenter(){
        interactor = new UpdateUserInteractor();
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

    public void onCreate(Context context){
        this.context = context;
        App.getInstance().getResponseListeners().getUsernameUpdateBus().subscribe(this);
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
            App.getInstance().updateCurrentUser();
            isAvailableToChange = "false";

            getViewState().onUpdateUsername();
        }
    }

    public void onDestroy() {
        super.onDestroy();
        App.getInstance().getResponseListeners().getUsernameUpdateBus().unsubscribe(this);
    }
}
