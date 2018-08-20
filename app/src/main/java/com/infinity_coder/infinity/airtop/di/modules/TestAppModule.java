package com.infinity_coder.infinity.airtop.di.modules;

import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.infinity_coder.infinity.airtop.data.db.AppDatabase;
import com.infinity_coder.infinity.airtop.data.prefs.app.AppPreference;
import com.infinity_coder.infinity.airtop.data.prefs.app.FakeAppPreference;
import com.infinity_coder.infinity.airtop.data.prefs.auth.AuthPreference;
import com.infinity_coder.infinity.airtop.utils.FakeServerPostman;
import com.infinity_coder.infinity.airtop.utils.ServerPostman;

public class TestAppModule extends AppModule {
    public TestAppModule(Context context){
        super(context);
    }

    @Override
    AppDatabase provideDatabase(){
        return Room.databaseBuilder(super.context, AppDatabase.class, "test_database.db")
                .setJournalMode(RoomDatabase.JournalMode.TRUNCATE)
                .fallbackToDestructiveMigration()
                .build();
    }

    @Override
    AppPreference provideAppPreference(){
        return new FakeAppPreference(context);
    }

    @Override
    AuthPreference provideAuthPreference(){
        return new AuthPreference(context);
    }

    @Override
    ServerPostman provideServerPostman() {
        return new FakeServerPostman();
    }
}
