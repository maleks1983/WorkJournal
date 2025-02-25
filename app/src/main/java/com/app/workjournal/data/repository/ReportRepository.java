package com.app.workjournal.data.repository;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.app.workjournal.data.db.AppDatabase;
import com.app.workjournal.data.db.CacheManager;
import com.app.workjournal.data.dto.JournalWithOperation;

import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ReportRepository {
    private final CacheManager cacheManager;
    private final AppDatabase database;
    private final ExecutorService executorService;
    private final Long userId;


    public ReportRepository(Context context) {
        executorService = Executors.newSingleThreadExecutor();
        this.cacheManager = CacheManager.getInstance();
        this.database = AppDatabase.getInstance(context);
        userId = Objects.requireNonNull(UserRepository.getInstance(context).getUser()).getId();
    }


    public void report(@NonNull Calendar calendar, MutableLiveData<List<JournalWithOperation>> journal) {
        try {
            executorService.submit(() -> {
                Calendar copyCalendar = (Calendar) calendar.clone();
                copyCalendar.set(Calendar.DATE, 1);
                copyCalendar.set(Calendar.HOUR_OF_DAY, 0);
                copyCalendar.set(Calendar.MINUTE, 0);
                copyCalendar.set(Calendar.SECOND, 0);
                copyCalendar.set(Calendar.MILLISECOND, 0);
                long startDay = copyCalendar.getTimeInMillis();
                copyCalendar.set(Calendar.DATE, Calendar.getInstance().getMaximum(Calendar.DAY_OF_MONTH));
                long endDay = copyCalendar.getTimeInMillis();
                journal.postValue(database.journalDao().getJournalWithOperationNameByUserIdReport(userId, startDay, endDay));
            });
        } catch (Exception e) {
            Log.e("ReportRepository", "report()", e);

        }

    }

    public void getWorkDayQuantity(Calendar calendar, MutableLiveData<Integer> day) {
        executorService.submit(() -> {
            try {
                Calendar copyCalendar = (Calendar) calendar.clone();
                copyCalendar.set(Calendar.DATE, 1);
                copyCalendar.set(Calendar.HOUR_OF_DAY, 0);
                copyCalendar.set(Calendar.MINUTE, 0);
                copyCalendar.set(Calendar.SECOND, 0);
                copyCalendar.set(Calendar.MILLISECOND, 0);
                long startDay = copyCalendar.getTimeInMillis();
                copyCalendar.set(Calendar.DATE, Calendar.getInstance().getMaximum(Calendar.DAY_OF_MONTH));
                long endDay = copyCalendar.getTimeInMillis();
                day.postValue(database.journalDao().getWorkDayQuantity(userId, startDay, endDay));
            } catch (Exception e) {
                Log.e("ReportRepository", "getWorkDayQuantity()", e);
            }
        });
    }

}
