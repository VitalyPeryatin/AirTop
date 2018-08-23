package com.infinity_coder.infinity.airtop;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.LruCache;
import android.util.Log;

import com.infinity_coder.infinity.airtop.data.db.AppDatabase;
import com.infinity_coder.infinity.airtop.data.db.interactors.UserInteractor;
import com.infinity_coder.infinity.airtop.data.db.model.User;
import com.infinity_coder.infinity.airtop.data.network.response.updaters.UpdateBioResponse;
import com.infinity_coder.infinity.airtop.data.network.response.updaters.UpdateNameResponse;
import com.infinity_coder.infinity.airtop.data.network.response.updaters.UpdateUsernameResponse;
import com.infinity_coder.infinity.airtop.data.prefs.app.AppPreference;
import com.infinity_coder.infinity.airtop.data.prefs.auth.AuthPreference;
import com.infinity_coder.infinity.airtop.di.components.AppComponent;
import com.infinity_coder.infinity.airtop.di.components.DaggerAppComponent;
import com.infinity_coder.infinity.airtop.di.modules.AppModule;
import com.infinity_coder.infinity.airtop.service.ClientService;
import com.infinity_coder.infinity.airtop.ui.auth.nickname.NicknameAuthBus;
import com.infinity_coder.infinity.airtop.ui.auth.phone_verify.PhoneVerifyBus;
import com.infinity_coder.infinity.airtop.ui.chat.MessageBus;
import com.infinity_coder.infinity.airtop.ui.contacts.ContactUpgradeBus;
import com.infinity_coder.infinity.airtop.ui.searchUser.SearchUserBus;
import com.infinity_coder.infinity.airtop.ui.settings.SettingsBus;
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

    public LiveData<User> getCurrentLiveUser() {
        String phone = appPreference.getCurrentPhone();
        return interactor.getLiveUserByPhone(phone);
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
        private PhoneVerifyBus phoneVerifyBus;
        private SettingsBus<UpdateUsernameResponse> usernameSettingsBus;
        private SettingsBus<UpdateNameResponse> nameSettingsBus;
        private SettingsBus<UpdateBioResponse> bioSettingsBus;
        private SearchUserBus searchUserBus;
        private NicknameAuthBus nicknameAuthBus;
        private ContactUpgradeBus contactUpgradeBus;

        ResponseListeners(){
            messageBus = new MessageBus();
            phoneVerifyBus = new PhoneVerifyBus();
            searchUserBus = new SearchUserBus();
            nicknameAuthBus = new NicknameAuthBus();
            contactUpgradeBus = new ContactUpgradeBus();

            usernameSettingsBus = new SettingsBus<>();
            nameSettingsBus = new SettingsBus<>();
            bioSettingsBus = new SettingsBus<>();
        }

        public MessageBus getMessageBus() {
            return messageBus;
        }

        public PhoneVerifyBus getPhoneAuthBus() {
            return phoneVerifyBus;
        }

        public SettingsBus<UpdateUsernameResponse> getUsernameSettingsBus() {
            return usernameSettingsBus;
        }

        public SettingsBus<UpdateNameResponse> getNameSettingsBus() {
            return nameSettingsBus;
        }

        public SettingsBus<UpdateBioResponse> getBioSettingsBus() {
            return bioSettingsBus;
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
