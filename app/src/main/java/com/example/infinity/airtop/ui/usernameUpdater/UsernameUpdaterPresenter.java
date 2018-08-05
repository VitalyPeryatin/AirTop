package com.example.infinity.airtop.ui.usernameUpdater;

import android.content.Context;
import android.content.Intent;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.infinity.airtop.data.db.interactors.ChatInteractor;
import com.example.infinity.airtop.data.db.model.User;
import com.example.infinity.airtop.data.network.CheckingUsername;
import com.example.infinity.airtop.data.network.UserRequest;
import com.example.infinity.airtop.service.ClientService;
import com.example.infinity.airtop.utils.JsonConverter;
import com.example.infinity.airtop.App;

import static com.example.infinity.airtop.data.network.CheckingUsername.RESULT_EMPTY;
import static com.example.infinity.airtop.data.network.CheckingUsername.RESULT_LITTLE;

@InjectViewState
public class UsernameUpdaterPresenter extends MvpPresenter<UsernameUpdaterView> implements OnUsernameUpdateListener{
    private Context context;
    private ChatInteractor chatInteractor;

    public UsernameUpdaterPresenter(){
        chatInteractor = new ChatInteractor();
    }

    public void onUpdateUsername(UserRequest userRequest){
        User user = new User(userRequest);
        chatInteractor.insertUser(user);
        App.getInstance().setCurrentUserPhone(user.phone);
        getViewState().onUpdateUsername();
    }

    public void onCreate(Context context){
        this.context = context;
        App.getInstance().getResponseListeners().getUsernameUpdateListener().subscribe(this);
    }

    public void onResultUsernameCheck(CheckingUsername checkingUsername){
        getViewState().onResultUsernameCheck(checkingUsername);
    }

    public void onTextChanged(String text){
        CheckingUsername checkingUsername = new CheckingUsername();
        if(text.length() == 0) {
            checkingUsername.setResult(RESULT_EMPTY);
            getViewState().onResultUsernameCheck(checkingUsername);
        }
        else if(text.length() < 5) {
            checkingUsername.setResult(RESULT_LITTLE);
            getViewState().onResultUsernameCheck(checkingUsername);
        }
        else {
            JsonConverter jsonConverter = new JsonConverter();
            String json = jsonConverter.toJson(new CheckingUsername());
            Intent intent = new Intent(context, ClientService.class);
            intent.putExtra("request", json);
            context.startService(intent);
            //App.getInstance().getBackendClient().sendRequest(new CheckingUsername(text));
        }
    }

    public void onDestroy() {
        super.onDestroy();
        App.getInstance().getResponseListeners().getUsernameUpdateListener().unsubscribe(this);
    }
}
