package com.infinity_coder.infinity.airtop.data.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.infinity_coder.infinity.airtop.data.db.model.Contact;
import com.infinity_coder.infinity.airtop.data.db.model.Message;
import com.infinity_coder.infinity.airtop.data.db.model.User;
import com.infinity_coder.infinity.airtop.data.db.repositoryDao.ContactDao;
import com.infinity_coder.infinity.airtop.data.db.repositoryDao.MessageDao;
import com.infinity_coder.infinity.airtop.data.db.repositoryDao.UserDao;

@Database(entities = {User.class, Contact.class, Message.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract ContactDao addresseeDao();
    public abstract MessageDao messageDao();
}
