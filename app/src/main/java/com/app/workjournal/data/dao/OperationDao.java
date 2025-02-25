package com.app.workjournal.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.app.workjournal.data.entity.Operation;

import java.util.List;

@Dao
public interface OperationDao {
    @Insert
    void insert(Operation operation);

    @Delete
    void delete(Operation operation);

    @Query("DELETE FROM operations")
    void deleteAll();

    @Update
    void update(Operation operation);

    @Query("SELECT * FROM operations")
    List<Operation> getAllOperations();


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Operation> operations);


    @Query("SELECT o.id, o.nameOperation FROM operations_user ou " +
            "JOIN operations o ON ou.operationId = o.id WHERE ou.userId = :userId ORDER BY o.nameOperation")
    List<Operation> getOperationsByUserId(Long userId);
}
