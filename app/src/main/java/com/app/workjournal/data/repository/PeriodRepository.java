package com.app.workjournal.data.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.app.workjournal.data.db.AppDatabase;
import com.app.workjournal.data.entity.MonthPeriod;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PeriodRepository {

    private final AppDatabase database;
    private final ExecutorService executorService;


    public PeriodRepository(Context context) {
        executorService = Executors.newSingleThreadExecutor();
        this.database = AppDatabase.getInstance(context);
    }

    public void updatePeriod(MonthPeriod period) {
        executorService.execute(() -> {
            try {
                if (database.periodDao().getCountPeriod(period.getId()) == 0) {
                    database.periodDao().insert(period);
                } else {
                    database.periodDao().update(period);
                }

            } catch (Exception e) {
                Log.e("DatabaseError", "Error updatePeriod()", e);
            }
        });

    }

    public void deletePeriod(MonthPeriod period) {
        executorService.execute(() -> {
            try {
                database.periodDao().delete(period);
            } catch (Exception e) {
                Log.e("DatabaseError", "Error deletePeriod()", e);
            }
        });
    }

    public LiveData<MonthPeriod> getMonthPeriod(int id) {
        try {
            MonthPeriod p = database.periodDao().getLiveData(id).getValue();
            return database.periodDao().getLiveData(id);
        } catch (Exception e) {
            Log.e("DatabaseError", "Error getMonthPeriod()", e);
            return null;
        }
    }
}
