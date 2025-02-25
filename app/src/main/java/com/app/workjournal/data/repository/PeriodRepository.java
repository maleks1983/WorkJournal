package com.app.workjournal.data.repository;

import android.content.Context;

import com.app.workjournal.data.db.AppDatabase;
import com.app.workjournal.data.db.CacheManager;

import java.time.Period;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PeriodRepository {
    private final CacheManager cacheManager;
    private final AppDatabase database;
    private final ExecutorService executorService;


    public PeriodRepository(Context context) {
        executorService = Executors.newSingleThreadExecutor();
        this.cacheManager = CacheManager.getInstance();
        this.database = AppDatabase.getInstance(context);
    }

    public Period getPeriod() {
        return null;
    }

}
