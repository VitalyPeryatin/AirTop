package com.example.infinity.airtop;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.infinity.airtop.data.db.model.User;
import com.example.infinity.airtop.data.network.UserRequest;
import com.example.infinity.airtop.ui.auth.AuthListener;
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
    private UserRequest currentUser;
    private static App instance;
    private AppDatabase database;
    private SharedPreferences sPref;
    private ResponseListeners responseListeners;

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

        database = Room.databaseBuilder(this, AppDatabase.class, "database.db")
                .setJournalMode(RoomDatabase.JournalMode.TRUNCATE)
                .fallbackToDestructiveMigration()
                .build();

        sPref = getSharedPreferences("savedUserPhone", MODE_PRIVATE);


        new Thread(() -> currentUser = loadCurrentUser()).start();


    }

    public AppDatabase getDatabase() {
        return database;
    }

    public UserRequest getCurrentUser() {
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

    public void setCurrentUserPhone(String currentUserPhone){
        SharedPreferences.Editor editor = sPref.edit();
        editor.putString(USER_PHONE_KEY, currentUserPhone);
        editor.apply();
        new Thread(() -> currentUser = loadCurrentUser()).start();
    }

    public void setCurrentUser(UserRequest currentUser) {
        this.currentUser = currentUser;
    }

    public ResponseListeners getResponseListeners() {
        return responseListeners;
    }

    private UserRequest loadCurrentUser(){
        final UserRequest userRequest;
        String currentUserPhone = sPref.getString(USER_PHONE_KEY, null);
        User user = database.userDao().getByPhone(currentUserPhone);
        if(currentUserPhone == null || user == null){
            userRequest = null;
        }
        else{
            userRequest = new UserRequest(user);
        }
        return userRequest;
    }

    public static class ResponseListeners {
        private MessageBus messageBus;
        private AuthListener authListener;
        private UsernameUpdateListener usernameUpdateListener;
        private SearchUserListener searchUserListener;

        ResponseListeners(){
            messageBus = new MessageBus();
            authListener = new AuthListener();
            usernameUpdateListener = new UsernameUpdateListener();
            searchUserListener = new SearchUserListener();
        }

        public MessageBus getMessageBus() {
            return messageBus;
        }

        public AuthListener getAuthListener() {
            return authListener;
        }

        public UsernameUpdateListener getUsernameUpdateListener() {
            return usernameUpdateListener;
        }

        public SearchUserListener getSearchUserListener() {
            return searchUserListener;
        }

    }

}
