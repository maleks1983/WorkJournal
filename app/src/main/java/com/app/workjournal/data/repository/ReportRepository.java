package com.app.workjournal.data.repository;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.app.workjournal.data.db.AppDatabase;
import com.app.workjournal.data.dto.JournalWithOperation;
import com.app.workjournal.data.entity.MonthPeriod;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ReportRepository {
    private final AppDatabase database;
    private final ExecutorService executorService;
    private final Long userId;


    public ReportRepository(Context context) {
        executorService = Executors.newSingleThreadExecutor();
        this.database = AppDatabase.getInstance(context);
        userId = Objects.requireNonNull(UserRepository.getInstance(context).getUser()).getId();
    }


    public void report(@NonNull Calendar calendar, MutableLiveData<List<JournalWithOperation>> journal) {
        try {
            executorService.submit(() -> {
                MonthPeriod period = getPeriod(calendar);
                journal.postValue(database.journalDao().getJournalWithOperationNameByUserIdReport(userId, period.getStartPeriod(), period.getEndPeriod()));
            });
        } catch (Exception e) {
            Log.e("ReportRepository", "report()", e);

        }

    }

    public void getWorkDayQuantity(Calendar calendar, MutableLiveData<Integer> day) {
        executorService.submit(() -> {
            try {
                MonthPeriod period = getPeriod(calendar);
                day.postValue(database.journalDao().getWorkDayQuantity(userId, period.getStartPeriod(), period.getEndPeriod()));
            } catch (Exception e) {
                Log.e("ReportRepository", "getWorkDayQuantity()", e);
            }
        });
    }

    @NonNull
    private MonthPeriod getPeriod(@NonNull Calendar calendar) {
        int id = Integer.parseInt(String.format(Locale.UK, "%d%02d", calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1));
        MonthPeriod period = database.periodDao().get(id);
        if (period == null) {
            Calendar copyCalendar = (Calendar) calendar.clone();
            copyCalendar.set(Calendar.DAY_OF_MONTH, 1);
            copyCalendar.set(Calendar.HOUR_OF_DAY, 0);
            copyCalendar.set(Calendar.MINUTE, 0);
            copyCalendar.set(Calendar.SECOND, 0);
            copyCalendar.set(Calendar.MILLISECOND, 0);
            long startDay = copyCalendar.getTimeInMillis();
            copyCalendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            long endDay = copyCalendar.getTimeInMillis();
            return new MonthPeriod(id, startDay, endDay);
        } else {
            return period;
        }

    }

}
