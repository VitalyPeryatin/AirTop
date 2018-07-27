package com.example.infinity.airtop.views;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;

import com.example.infinity.airtop.models.databases.AppDatabase;
import com.example.infinity.airtop.models.User;
import com.example.infinity.airtop.models.databases.UserDao;
import com.example.infinity.airtop.presenters.client.BackendClient;

public class App extends Application {
    private static BackendClient backendClient; // TODO Зачем здесь static?
    private User currentUser;
    private static App instance;
    private AppDatabase database;

    public synchronized static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        database = Room.databaseBuilder(this, AppDatabase.class, "database.db")
                .setJournalMode(RoomDatabase.JournalMode.TRUNCATE)
                .build();
        backendClient = new BackendClient();

    }

    public AppDatabase getDatabase() {
        return database;
    }

    public static BackendClient getBackendClient() {
        return backendClient;
    }

    public void setCurrentUser(User user){
        currentUser = user;
    }

    public User getCurrentUser() {
        return currentUser;
    }
}
