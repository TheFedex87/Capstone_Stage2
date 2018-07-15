package com.udacity.thefedex87.takemyorder.room.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.udacity.thefedex87.takemyorder.room.entity.User;

import java.util.List;

/**
 * Created by feder on 30/06/2018.
 */
@Dao
public interface UserDao {
    @Query("SELECT * FROM user")
    LiveData<List<User>> getUsers();

    @Query("SELECT * FROM user WHERE userFirebaseId = :userFirebaseId")
    LiveData<User> getUserByUserFirebaseId(String userFirebaseId);

    @Query("SELECT * FROM user WHERE userFirebaseId = :userFirebaseId")
    User getUserByUserFirebaseIdWidget(String userFirebaseId);

    @Insert
    long insertUser(User user);
}
