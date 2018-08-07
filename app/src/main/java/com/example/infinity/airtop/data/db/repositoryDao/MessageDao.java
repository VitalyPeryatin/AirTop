package com.example.infinity.airtop.data.db.repositoryDao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.infinity.airtop.data.db.model.Addressee;
import com.example.infinity.airtop.data.db.model.Message;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public abstract class MessageDao {
    @Query("SELECT * FROM message")
    public abstract List<Message> getAll();

    @Insert
    public abstract void insert(Message message);

    @Query("SELECT * FROM message WHERE addressId=:uuid")
    public abstract List<Message> getMessagesByUUID(String uuid);

    @Query("SELECT * FROM message WHERE addressId=:uuid ORDER BY id DESC LIMIT 1;")
    public abstract Message getLastMessageByAddressId(String uuid);

    @Query("DELETE FROM message WHERE addressId=:addressId")
    public abstract void deleteByAddressId(String addressId);

}
