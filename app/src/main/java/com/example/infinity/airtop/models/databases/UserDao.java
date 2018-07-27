package com.example.infinity.airtop.models.databases;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.infinity.airtop.models.User;

import java.util.List;

@Dao
public abstract class UserDao {
    @Query("SELECT * FROM user")
    public abstract List<User> getAll();

    @Query("SELECT * FROM user WHERE id=:id")
    public abstract User getById(long id);

    @Query("SELECT * FROM user WHERE phone=:phone")
    public abstract User getByPhone(String phone);

    @Insert
    public abstract void insert(User user);

    @Update
    public abstract void update(User user);

    @Delete
    public abstract void delete(User user);

    public int getSize(){
        return getAll().size();
    }
}
