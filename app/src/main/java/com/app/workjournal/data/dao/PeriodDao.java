package com.app.workjournal.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.app.workjournal.data.entity.User;

import java.time.Period;

@Dao
public interface PeriodDao {
    @Insert
    void insert(Period period);

    @Delete
    void delete(Period period);

    @Update
    void update(Period period);

    @Query("SELECT * FROM month_period as p WHERE p.id = :id")
    User get(Integer id);

}
