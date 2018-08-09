package com.example.infinity.airtop;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Intent;

import com.example.infinity.airtop.data.db.interactors.UserInteractor;
import com.example.infinity.airtop.data.db.model.User;
import com.example.infinity.airtop.data.prefs.app.AppPreference;
import com.example.infinity.airtop.service.ClientService;
import com.example.infinity.airtop.ui.auth.nickname.NicknameAuthBus;
import com.example.infinity.airtop.ui.auth.phone.PhoneAuthBus;
import com.example.infinity.airtop.ui.chat.MessageBus;
import com.example.infinity.airtop.data.db.AppDatabase;
import com.example.infinity.airtop.ui.searchUser.SearchUserBus;
import com.example.infinity.airtop.ui.settings.updaters.username.UsernameUpdateBus;
import com.facebook.stetho.Stetho;

public class App extends Application {
    private User currentUser;
    private static App instance;
    private AppDatabase database;
    private ResponseListeners responseListeners;
    private UserInteractor interactor;

    private static final String USER_PHONE_KEY = "user_phone_key";

    public synchronized static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Stetho.Initializer initializer = Stetho.newInitializerBuilder(this)
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                .build();

        Stetho.initialize(initializer);

        instance = this;
        responseListeners = new ResponseListeners();
        interactor = new UserInteractor();

        Intent intent = new Intent(getBaseContext(), ClientService.class);
        startService(intent);

        database = Room.databaseBuilder(this, AppDatabase.class, "database.db")
                .setJournalMode(RoomDatabase.JournalMode.TRUNCATE)
                .fallbackToDestructiveMigration()
                .build();
        updateCurrentUser();
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
        currentUser = interactor.getUserByPhone(new AppPreference().getCurrentPhone());
    }

    public static class ResponseListeners {
        private MessageBus messageBus;
        private PhoneAuthBus phoneAuthBus;
        private UsernameUpdateBus usernameUpdateBus;
        private SearchUserBus searchUserBus;
        private NicknameAuthBus nicknameAuthBus;

        ResponseListeners(){
            messageBus = new MessageBus();
            phoneAuthBus = new PhoneAuthBus();
            usernameUpdateBus = new UsernameUpdateBus();
            searchUserBus = new SearchUserBus();
            nicknameAuthBus = new NicknameAuthBus();
        }

        public MessageBus getMessageBus() {
            return messageBus;
        }

        public PhoneAuthBus getPhoneAuthBus() {
            return phoneAuthBus;
        }

        public UsernameUpdateBus getUsernameUpdateBus() {
            return usernameUpdateBus;
        }

        public SearchUserBus getSearchUserBus() {
            return searchUserBus;
        }

        public NicknameAuthBus getNicknameAuthBus() {
            return nicknameAuthBus;
        }
    }

}
