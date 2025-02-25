package com.app.workjournal.data.repository;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.app.workjournal.data.db.AppDatabase;
import com.app.workjournal.data.db.CacheManager;
import com.app.workjournal.data.dto.JournalWithOperation;
import com.app.workjournal.data.entity.Journal;
import com.app.workjournal.data.entity.Operation;
import com.app.workjournal.data.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HomeRepository {

    private final CacheManager cacheManager;
    private final AppDatabase database;
    private final ExecutorService executorService;
    private final Long userId;


    public HomeRepository(Context context) {
        cacheManager = CacheManager.getInstance();
        database = AppDatabase.getInstance(context);
        executorService = Executors.newSingleThreadExecutor();
        userId = Objects.requireNonNull(UserRepository.getInstance(context).getUser()).getId();

    }



    public void getUserOperationsSelected(MutableLiveData<List<Operation>> operationsMediatorLiveData) {
        if (userId == null) {
            Log.e("HomeRepository", "UserId is null. Cannot fetch data.");
            operationsMediatorLiveData.postValue(new ArrayList<>());
        }
        executorService.submit(() -> {
            try {
                List<Operation> operation = cacheManager.getOperationsHome();
                if (Objects.isNull(operation)) {
                    operation = database.operationDao().getOperationsByUserId(userId);
                }
                cacheManager.putOperationsHome(operation);
                operationsMediatorLiveData.postValue(operation);
            } catch (Exception e) {
                Log.e("DatabaseError", "Error getUserOperations()", e);
                operationsMediatorLiveData.postValue(new ArrayList<>());
            }
        });
    }

    public void getJournalUserByDate(MutableLiveData<List<JournalWithOperation>> journalLive, Long day) {
        if (userId == null) {
            Log.e("HomeRepository", "UserId is null. Cannot fetch data.");
            journalLive.postValue(new ArrayList<>());
        }
        executorService.submit(() -> {
            try {
                List<JournalWithOperation> journal = cacheManager.getJournal(day);
                if (journal == null) {
                    journal = database.journalDao().getJournalWithOperationNameByUserId(userId, day);
                }
                cacheManager.putJournal(day, journal);
                journalLive.postValue(journal);

            } catch (Exception e) {
                Log.e("DatabaseError", "Error getJournalUserByDate()", e);
                journalLive.postValue(new ArrayList<>());
            }
        });
    }

    public void deleteJournal(Journal journal) {
        executorService.execute(() -> {
            try {
                database.journalDao().delete(journal);
                cacheManager.putJournal(journal.getDate(), database.journalDao().getJournalWithOperationNameByUserId(userId, journal.getDate()));
            } catch (Exception e) {
                Log.e("DatabaseError", "Error deleting operations", e);
            }
        });
    }

    public void updateJournal(@NonNull Journal journal) {
        journal.setUserId(userId);
        try {
            executorService.execute(() -> {
                if (!journal.isNew()) {
                    database.journalDao().update(journal);
                } else {
                    Journal existingJournal = database.journalDao().get(journal.getUserId(), journal.getDate(), journal.getOperationId());
                    if (existingJournal == null) {
                        database.journalDao().insert(journal);
                    } else {
                        existingJournal.setQuantity(existingJournal.getQuantity() + journal.getQuantity());
                        database.journalDao().update(existingJournal);
                    }
                }
                cacheManager.putJournal(journal.getDate(), database.journalDao().getJournalWithOperationNameByUserId(userId, journal.getDate()));
            });

        } catch (Exception e) {
            Log.e("DatabaseError", "Error updating journal", e);
        }

    }
}
