package com.codegama.productivity_booster.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.codegama.productivity_booster.model.User;

@Dao
public interface user {

    @Insert
    void registerUser(User userEntity);

    @Query("SELECT * from User where email=(:email) and password=(:password)")
    User login(String email,String password);
}
