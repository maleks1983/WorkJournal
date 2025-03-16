package com.app.workjournal.data.db;

import androidx.annotation.NonNull;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

public class Migrations {
    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase supportSQLiteDatabase) {
            supportSQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS `month_period` (" +
                    "`id` INTEGER NOT NULL PRIMARY KEY, " +
                    "`startPeriod` INTEGER, " +
                    "`endPeriod` INTEGER)");
        }

    };
}
