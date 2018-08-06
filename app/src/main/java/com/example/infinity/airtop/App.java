package com.example.infinity.airtop;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Intent;
import android.util.Log;

import com.example.infinity.airtop.data.db.interactors.UserInteractor;
import com.example.infinity.airtop.data.db.model.User;
import com.example.infinity.airtop.data.network.UserRequest;
import com.example.infinity.airtop.data.prefs.app.AppPreference;
import com.example.infinity.airtop.ui.auth.nickname.NicknameAuthListener;
import com.example.infinity.airtop.ui.auth.phone.PhoneAuthListener;
import com.example.infinity.airtop.ui.chat.MessageBus;
import com.example.infinity.airtop.data.network.PhoneVerifier;
import com.example.infinity.airtop.data.db.AppDatabase;
import com.example.infinity.airtop.data.db.repositoryDao.UserDao;
import com.example.infinity.airtop.service.ClientService;
import com.example.infinity.airtop.utils.JsonConverter;
import com.example.infinity.airtop.ui.searchUser.SearchUserListener;
import com.example.infinity.airtop.ui.usernameUpdater.UsernameUpdateListener;
import com.facebook.stetho.Stetho;

import java.util.ArrayList;

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

    public void verifyUserPhone(){
        new Thread(() -> {
            UserDao userDao = App.getInstance().getDatabase().userDao();
            ArrayList<User> users = (ArrayList<User>) userDao.getAll();
            ArrayList<UserRequest> usersRequest = new ArrayList<>();
            for (User user : users) {
                usersRequest.add(new UserRequest(user));
            }

            Log.d("mLog", "" + users.size());
            for (UserRequest user : usersRequest) {
                Log.d("mLog", user.username);
                PhoneVerifier phoneVerifier = new PhoneVerifier();
                phoneVerifier.userPhone = user.phone;
                JsonConverter jsonConverter = new JsonConverter();
                String json = jsonConverter.toJson(phoneVerifier);
                Intent intent = new Intent(getBaseContext(), ClientService.class);
                intent.putExtra("request", json);
                getBaseContext().startService(intent);
            }
        }).start();
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
        private PhoneAuthListener phoneAuthListener;
        private UsernameUpdateListener usernameUpdateListener;
        private SearchUserListener searchUserListener;
        private NicknameAuthListener nicknameAuthListener;

        ResponseListeners(){
            messageBus = new MessageBus();
            phoneAuthListener = new PhoneAuthListener();
            usernameUpdateListener = new UsernameUpdateListener();
            searchUserListener = new SearchUserListener();
            nicknameAuthListener = new NicknameAuthListener();
        }

        public MessageBus getMessageBus() {
            return messageBus;
        }

        public PhoneAuthListener getPhoneAuthListener() {
            return phoneAuthListener;
        }

        public UsernameUpdateListener getUsernameUpdateListener() {
            return usernameUpdateListener;
        }

        public SearchUserListener getSearchUserListener() {
            return searchUserListener;
        }

        public NicknameAuthListener getNicknameAuthListener() {
            return nicknameAuthListener;
        }
    }

}
