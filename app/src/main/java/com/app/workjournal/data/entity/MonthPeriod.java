package com.app.workjournal.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "month_period")
public class MonthPeriod {
    @PrimaryKey(autoGenerate = false)
    Integer id;
    Long startPeriod;
    Long endPeriod;

    public MonthPeriod(Integer id, Long startPeriod, Long endPeriod) {
        this.id = id;
        this.startPeriod = startPeriod;
        this.endPeriod = endPeriod;
    }

    public MonthPeriod(long startPeriod, long endPeriod) {
        this.startPeriod = startPeriod;
        this.endPeriod = endPeriod;
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
