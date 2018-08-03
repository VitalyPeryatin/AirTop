package com.example.infinity.airtop.data.db.repositoryDao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.infinity.airtop.data.db.model.Message;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public abstract class MessageDao {
    @Query("SELECT * FROM message")
    public abstract List<Message> getAll();

    @Insert
    public abstract void insert(Message user);

    @Query("SELECT * FROM message WHERE addressee_phone=:phone")
    public abstract List<Message> getByAddresseePhone(String phone);

    @Query("SELECT * FROM message WHERE sender_phone=:phone")
    public abstract List<Message> getBySenderPhone(String phone);

    @Delete
    public abstract void delete(Message user);
}
