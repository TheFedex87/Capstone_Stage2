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
    //Get all the users saved into the DB
    @Query("SELECT * FROM user")
    LiveData<List<User>> getUsers();

    //Get the user by the firebaseUserId
    @Query("SELECT * FROM user WHERE userFirebaseId = :userFirebaseId")
    LiveData<User> getUserByUserFirebaseId(String userFirebaseId);

    //Get the user by the firebaseUserId without LiveData (used in Tests)
    @Query("SELECT * FROM user WHERE userFirebaseId = :userFirebaseId")
    User getUserByUserFirebaseIdForTests(String userFirebaseId);

    @Insert
    long insertUser(User user);
}
