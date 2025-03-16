package com.app.workjournal.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.app.workjournal.data.entity.MonthPeriod;

@Dao
public interface PeriodDao {
    @Insert
    void insert(MonthPeriod period);

    @Delete
    void delete(MonthPeriod period);

    @Update
    void update(MonthPeriod period);

    @Query("SELECT * FROM month_period as p WHERE p.id = :id LIMIT 1")
    LiveData<MonthPeriod> getLiveData(int id);

    @Query("SELECT COUNT(DISTINCT id ) FROM  month_period WHERE id = :id")
    Integer getCountPeriod(int id);

    @Query("SELECT * FROM month_period as p WHERE p.id = :id LIMIT 1")
    MonthPeriod get(int id);

}
