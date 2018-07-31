package com.example.infinity.airtop.models.databases;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.infinity.airtop.models.User;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public abstract class UserDao {
    @Query("SELECT * FROM user")
    public abstract List<User> getAll();

    @Query("SELECT * FROM user WHERE phone=:phone")
    public abstract User getByPhone(String phone);

    @Insert(onConflict = REPLACE)
    public abstract void insert(User user);

    @Delete
    public abstract void delete(User user);

    public int getSize(){
        return getAll().size();
    }
}
