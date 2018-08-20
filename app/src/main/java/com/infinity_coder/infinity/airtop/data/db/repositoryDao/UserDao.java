package com.infinity_coder.infinity.airtop.data.db.repositoryDao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.infinity_coder.infinity.airtop.data.db.model.User;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public abstract class UserDao {
    @Query("SELECT * FROM user")
    public abstract List<User> getAll();

    @Query("SELECT * FROM user WHERE phone=:phone")
    public abstract User getByPhone(String phone);

    @Query("SELECT * FROM user WHERE phone=:phone")
    public abstract LiveData<User> getUserByPhone(String phone);

    @Insert(onConflict = REPLACE)
    public abstract void insert(User user);

    @Query("UPDATE user SET username=:username WHERE uuid=:uuid")
    public abstract void updateUsername(String uuid, String username);

    @Query("UPDATE user SET nickname=:name WHERE uuid=:uuid")
    public abstract void updateName(String uuid, String name);

    public int getSize(){
        return getAll().size();
    }
}
