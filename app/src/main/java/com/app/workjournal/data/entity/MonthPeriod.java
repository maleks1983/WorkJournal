package com.app.workjournal.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "month_period")
public class MonthPeriod {
    @PrimaryKey(autoGenerate = false)
    Integer id;
    Long startPeriod;
    Long endPeriod;
}
