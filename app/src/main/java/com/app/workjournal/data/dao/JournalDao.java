package com.app.workjournal.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.app.workjournal.data.dto.JournalWithOperation;
import com.app.workjournal.data.entity.Journal;

import java.util.List;
import java.util.Set;

@Dao
public interface JournalDao {

    @Delete
    void delete(Journal journal);

    @Update
    void update(Journal journal);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Journal journal);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Set<Journal> journalList);

    @Query("SELECT journal.*, nameOperation as operationName FROM journal  " +
            "LEFT JOIN operations ON journal.operationId = operations.id " +
            "WHERE journal.userId = :userId AND journal.date = :day")
    List<JournalWithOperation> getJournalWithOperationNameByUserId(Long userId, long day);

    @Query("SELECT journal.*, nameOperation as operationName, SUM(quantity) AS quantity FROM journal " +
            "LEFT JOIN operations ON journal.operationId = operations.id " +
            "WHERE journal.userId = :userId AND journal.date BETWEEN  :startDay and :endDay GROUP BY operationId")
    List<JournalWithOperation> getJournalWithOperationNameByUserIdReport(Long userId, long startDay, long endDay);


    @Query("SELECT * FROM journal  WHERE userId = :userId AND date = :date AND operationId =:operation_id ")
    Journal get(Long userId, Long date, int operation_id);

    @Query("SELECT * FROM journal  WHERE id = :id")
    Journal getById(Integer id);

    @Query("SELECT COUNT(DISTINCT date / 86400000) FROM journal WHERE userId = :userId AND date BETWEEN :startDate AND :endDate")
    Integer getWorkDayQuantity(Long userId, Long startDate, Long endDate);
}