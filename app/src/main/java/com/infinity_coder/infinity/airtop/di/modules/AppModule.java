package com.infinity_coder.infinity.airtop.di.modules;

import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.infinity_coder.infinity.airtop.data.db.AppDatabase;
import com.infinity_coder.infinity.airtop.data.prefs.app.AppPreference;
import com.infinity_coder.infinity.airtop.data.prefs.auth.AuthPreference;
import com.infinity_coder.infinity.airtop.utils.ServerPostman;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    protected Context context;

    public AppModule(Context context){
        this.context = context;
    }

    @Provides
    AppDatabase provideDatabase(){
        return Room.databaseBuilder(this.context, AppDatabase.class, "database.db")
                .setJournalMode(RoomDatabase.JournalMode.TRUNCATE)
                .fallbackToDestructiveMigration()
                .build();
    }

    @Provides
    AppPreference provideAppPreference(){
        return new AppPreference(context);
    }

    @Provides
    AuthPreference provideAuthPreference(){
        return new AuthPreference(context);
    }

    @Provides
    ServerPostman provideServerPostman(){
        return new ServerPostman();
    }
}
