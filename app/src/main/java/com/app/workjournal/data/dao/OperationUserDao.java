package com.app.workjournal.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.app.workjournal.data.entity.OperationUser;

import java.util.List;

@Dao
public interface OperationUserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(OperationUser operation);

    @Delete
    void delete(OperationUser operation);

    @Query("DELETE FROM operations_user WHERE userId = :userId")
    void deleteAll(Long userId);

    @Update
    void update(OperationUser operation);

    @Query("SELECT * FROM operations_user WHERE userId = :userId")
    List<OperationUser> getAll(Long userId);

    @Insert
    void insertAll(List<OperationUser> operations);
}
