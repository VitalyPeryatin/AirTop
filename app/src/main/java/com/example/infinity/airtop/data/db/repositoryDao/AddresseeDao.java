package com.example.infinity.airtop.data.db.repositoryDao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.infinity.airtop.data.db.model.Addressee;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public abstract class AddresseeDao {
    @Query("SELECT * FROM addressee")
    public abstract List<Addressee> getAll();

    @Query("SELECT nickname FROM addressee WHERE uuid=:uuid")
    public abstract String getNicknameById(String uuid);

    @Insert(onConflict = REPLACE)
    public abstract void insert(Addressee user);

    @Delete
    public abstract void delete(Addressee user);

    public int getSize(){
        return getAll().size();
    }
}
