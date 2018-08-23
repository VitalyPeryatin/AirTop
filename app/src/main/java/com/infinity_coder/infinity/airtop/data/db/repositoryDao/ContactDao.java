package com.infinity_coder.infinity.airtop.data.db.repositoryDao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.infinity_coder.infinity.airtop.data.db.model.Contact;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public abstract class ContactDao {
    @Query("SELECT * FROM Contact")
    public abstract List<Contact> getAll();

    @Query("SELECT uuid FROM Contact")
    public abstract List<String> getUuidList();

    @Query("SELECT * FROM Contact")
    public abstract LiveData<List<Contact>> getAllLive();

    @Query("SELECT nickname FROM Contact WHERE uuid=:uuid")
    public abstract String getNicknameById(String uuid);

    @Query("SELECT username FROM Contact WHERE uuid=:uuid")
    public abstract String getUsernameById(String uuid);

    @Query("SELECT * FROM Contact WHERE uuid=:uuid")
    public abstract LiveData<Contact> getLiveAddresseeById(String uuid);

    @Query("SELECT * FROM Contact WHERE uuid=:uuid")
    public abstract Contact getAddresseeById(String uuid);

    @Insert(onConflict = REPLACE)
    public abstract void insert(Contact user);

    @Delete
    public abstract void delete(Contact user);
}
