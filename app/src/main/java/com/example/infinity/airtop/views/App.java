package com.example.infinity.airtop.views;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.infinity.airtop.models.PhoneVerifier;
import com.example.infinity.airtop.models.databases.AppDatabase;
import com.example.infinity.airtop.models.User;
import com.example.infinity.airtop.models.databases.DatabaseHandler;
import com.example.infinity.airtop.models.databases.UserDao;
import com.example.infinity.airtop.services.client.BackendClient;
import com.facebook.stetho.Stetho;

import java.util.ArrayList;

public class App extends Application {
    private BackendClient backendClient;
    private User currentUser;
    private static App instance;
    private AppDatabase database;
    private String currentUserPhone = null;
    private SharedPreferences sPref;

    private static final String USER_PHONE_KEY = "user_phone_key";

    public synchronized static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // Create an InitializerBuilder
        Stetho.InitializerBuilder initializerBuilder =
                Stetho.newInitializerBuilder(this);

        // Enable Chrome DevTools
        initializerBuilder.enableWebKitInspector(
                Stetho.defaultInspectorModulesProvider(this)
        );

        // Enable command line interface
        initializerBuilder.enableDumpapp(
                Stetho.defaultDumperPluginsProvider(this)
        );

        // Use the InitializerBuilder to generate an Initializer
        Stetho.Initializer initializer = initializerBuilder.build();

        // Initialize Stetho with the Initializer
        Stetho.initialize(initializer);

        instance = this;
        database = Room.databaseBuilder(this, AppDatabase.class, "database.db")
                .setJournalMode(RoomDatabase.JournalMode.TRUNCATE)
                .build();
        backendClient = new BackendClient();

        sPref = getSharedPreferences("savedUserPhone", MODE_PRIVATE);

        currentUserPhone = loadCurrentUser();
        if(currentUserPhone != null)
            verifyUser();

    }

    public AppDatabase getDatabase() {
        return database;
    }

    public BackendClient getBackendClient() {
        return backendClient;
    }

    public void setCurrentUser(User user){
        currentUser = user;
    }

    public User getCurrentUser() {
        return currentUser;
    }



    public void verifyUser(){
        DatabaseHandler databaseHandler = DatabaseHandler.getInstance();
        databaseHandler.runByTag(DatabaseHandler.GET_USER_BY_PHONE, currentUserPhone);

        verifyPhone();
    }

    private void verifyPhone(){
        new Thread(() -> {
            UserDao userDao = App.getInstance().getDatabase().userDao();
            ArrayList<User> users = (ArrayList<User>) userDao.getAll();
            Log.d("mLog", "" + users.size());
            for (User user : users) {
                Log.d("mLog", user.username);
                PhoneVerifier phoneVerifier = new PhoneVerifier();
                phoneVerifier.userPhone = user.phone;
                App.getInstance().getBackendClient().sendRequest(phoneVerifier);
            }
        }).start();
    }

    private void saveCurrentUser(){
        SharedPreferences.Editor editor = sPref.edit();
        editor.putString(USER_PHONE_KEY, currentUserPhone);
        editor.apply();
    }

    private String loadCurrentUser(){
        return sPref.getString(USER_PHONE_KEY, null);
    }
}
