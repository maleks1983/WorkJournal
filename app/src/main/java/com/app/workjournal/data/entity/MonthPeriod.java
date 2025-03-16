package com.app.workjournal.data.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Calendar;
import java.util.Locale;

@Entity(tableName = "month_period")
public class MonthPeriod {
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    @NonNull
    Integer id;
    Long startPeriod;
    Long endPeriod;

    public MonthPeriod(@NonNull Integer id, Long startPeriod, Long endPeriod) {
        this.id = id;
        this.startPeriod = startPeriod;
        this.endPeriod = endPeriod;
    }

    @Ignore
    public MonthPeriod(long startPeriod, long endPeriod) {
        this.startPeriod = startPeriod;
        this.endPeriod = endPeriod;
        id = 0;
    }

    @Ignore
    public MonthPeriod(int year, int month) {
        this.id = Integer.parseInt(String.format(Locale.UK, "%d%02d", year, month));

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month-1);

        calendar.set(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        this.startPeriod = calendar.getTimeInMillis();

        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 99);
        this.endPeriod = calendar.getTimeInMillis();
    }

    @NonNull
    public Integer getId() {
        return id;
    }

    public void setId(@NonNull Integer id) {
        this.id = id;
    }

    public Long getStartPeriod() {
        return startPeriod;
    }

    public void setStartPeriod(Long startPeriod) {
        this.startPeriod = startPeriod;
    }

    public Long getEndPeriod() {
        return endPeriod;
    }

    public void setEndPeriod(Long endPeriod) {
        this.endPeriod = endPeriod;
    }
}
