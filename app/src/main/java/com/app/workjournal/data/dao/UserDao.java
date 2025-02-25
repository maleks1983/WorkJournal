package com.app.workjournal.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.app.workjournal.data.entity.User;

import java.util.List;

@Dao
public interface UserDao {
    @Insert
    void insert(User user);

    @Delete
    void delete(User user);

    @Query("DELETE FROM users")
    void deleteAll();

    @Update
    void update(User user);

    @Query("SELECT * FROM users as u WHERE u.id = :id")
    User get(Long id);

    @Query("SELECT * FROM users as u WHERE u.id = :id")
    LiveData<User> getUserLiveData(Long id);
}
