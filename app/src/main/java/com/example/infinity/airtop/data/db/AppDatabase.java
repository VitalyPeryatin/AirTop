package com.example.infinity.airtop.data.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.infinity.airtop.data.db.model.Addressee;
import com.example.infinity.airtop.data.db.model.User;
import com.example.infinity.airtop.data.db.repositoryDao.AddresseeDao;
import com.example.infinity.airtop.data.db.repositoryDao.UserDao;

@Database(entities = {User.class, Addressee.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract AddresseeDao addresseeDao();
}
