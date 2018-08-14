package com.example.infinity.airtop;

import android.app.Application;
import android.content.Intent;

import com.example.infinity.airtop.data.db.interactors.UserInteractor;
import com.example.infinity.airtop.data.db.model.User;
import com.example.infinity.airtop.data.network.response.MessageResponse;
import com.example.infinity.airtop.data.network.response.updaters.UpdateNameResponse;
import com.example.infinity.airtop.data.network.response.updaters.UpdateUsernameResponse;
import com.example.infinity.airtop.data.prefs.app.AppPreference;
import com.example.infinity.airtop.data.prefs.auth.AuthPreference;
import com.example.infinity.airtop.di.components.AppComponent;
import com.example.infinity.airtop.di.components.DaggerAppComponent;
import com.example.infinity.airtop.di.modules.AppModule;
import com.example.infinity.airtop.service.ClientService;
import com.example.infinity.airtop.ui.auth.nickname.NicknameAuthBus;
import com.example.infinity.airtop.ui.auth.phone.PhoneAuthBus;
import com.example.infinity.airtop.ui.chat.MessageBus;
import com.example.infinity.airtop.data.db.AppDatabase;
import com.example.infinity.airtop.ui.contacts.ContactUpgradeBus;
import com.example.infinity.airtop.ui.searchUser.SearchUserBus;
import com.example.infinity.airtop.ui.settings.SettingsBus;
import com.facebook.stetho.Stetho;

import javax.inject.Inject;

public class App extends Application {
    private User currentUser;
    private static App instance;
    @Inject
    AppDatabase database;
    @Inject
    AppPreference appPreference;
    @Inject
    AuthPreference authPreference;
    private ResponseListeners responseListeners;
    private UserInteractor interactor;

    private static final String USER_PHONE_KEY = "user_phone_key";

    public synchronized static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        AppComponent appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this)).build();
        appComponent.inject(this);
        Stetho.Initializer initializer = Stetho.newInitializerBuilder(this)
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                .build();

        Stetho.initialize(initializer);

        instance = this;
        responseListeners = new ResponseListeners();
        interactor = new UserInteractor();

        Intent intent = new Intent(getBaseContext(), ClientService.class);
        startService(intent);

        updateCurrentUser();
    }

    public AppPreference getAppPreference() {
        return appPreference;
    }

    public AuthPreference getAuthPreference() {
        return authPreference;
    }

    public AppDatabase getDatabase() {
        return database;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public ResponseListeners getResponseListeners() {
        return responseListeners;
    }

    public void updateCurrentUser(){
        currentUser = interactor.getUserByPhone(appPreference.getCurrentPhone());
    }

    public static class ResponseListeners {
        private MessageBus messageBus;
        private PhoneAuthBus phoneAuthBus;
        private SettingsBus<UpdateUsernameResponse> usernameSettingsBus;
        private SettingsBus<UpdateNameResponse> nameSettingsBus;
        private SearchUserBus searchUserBus;
        private NicknameAuthBus nicknameAuthBus;
        private ContactUpgradeBus contactUpgradeBus;

        ResponseListeners(){
            messageBus = new MessageBus();
            phoneAuthBus = new PhoneAuthBus();
            searchUserBus = new SearchUserBus();
            nicknameAuthBus = new NicknameAuthBus();
            contactUpgradeBus = new ContactUpgradeBus();

            usernameSettingsBus = new SettingsBus<>();
            nameSettingsBus = new SettingsBus<>();
        }

        public MessageBus getMessageBus() {
            return messageBus;
        }

        public PhoneAuthBus getPhoneAuthBus() {
            return phoneAuthBus;
        }

        public SettingsBus<UpdateUsernameResponse> getUsernameSettingsBus() {
            return usernameSettingsBus;
        }

        public SettingsBus<UpdateNameResponse> getNameSettingsBus() {
            return nameSettingsBus;
        }

        public SearchUserBus getSearchUserBus() {
            return searchUserBus;
        }

        public NicknameAuthBus getNicknameAuthBus() {
            return nicknameAuthBus;
        }

        public ContactUpgradeBus getContactUpgradeBus() {
            return contactUpgradeBus;
        }

    }

}
