package com.udacity.thefedex87.takemyorder.room.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Created by feder on 30/06/2018.
 */
//Tables which joins the many-to-many relation beetween favourite meal and users
@Entity(tableName = "favouritemeal_user_join",
    primaryKeys = { "userId", "favouriteMealId" },
    foreignKeys = {
        @ForeignKey(entity = FavouriteMeal.class,
                    parentColumns = "id",
                    childColumns = "favouriteMealId",
                    onDelete = CASCADE),

        @ForeignKey(entity = User.class,
                    parentColumns = "id",
                    childColumns = "userId",
                    onDelete = CASCADE)
    })
public class FavouriteMealUserJoin {
    public long userId;
    public long favouriteMealId;

    public FavouriteMealUserJoin(long userId, long favouriteMealId) {
        this.userId = userId;
        this.favouriteMealId = favouriteMealId;
    }
}
