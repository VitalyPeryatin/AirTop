package com.infinity_coder.infinity.airtop.data.db.repositoryDao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.infinity_coder.infinity.airtop.data.db.model.Message;

import java.util.List;

@Dao
public abstract class MessageDao {
    @Insert
    public abstract void insert(Message message);

    @Query("SELECT * FROM message WHERE addressId=:uuid")
    public abstract List<Message> getMessagesByUUID(String uuid);

    @Query("SELECT * FROM message WHERE addressId=:uuid ORDER BY id DESC LIMIT 1;")
    public abstract Message getLastMessageByAddressId(String uuid);

    @Query("DELETE FROM message WHERE addressId=:addressId")
    public abstract void deleteByAddressId(String addressId);

}
