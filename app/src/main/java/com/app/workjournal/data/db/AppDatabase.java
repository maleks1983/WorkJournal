package com.app.workjournal.data.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.app.workjournal.data.dao.JournalDao;
import com.app.workjournal.data.dao.OperationDao;
import com.app.workjournal.data.dao.OperationUserDao;
import com.app.workjournal.data.dao.PeriodDao;
import com.app.workjournal.data.dao.UserDao;
import com.app.workjournal.data.entity.Journal;
import com.app.workjournal.data.entity.MonthPeriod;
import com.app.workjournal.data.entity.Operation;
import com.app.workjournal.data.entity.OperationUser;
import com.app.workjournal.data.entity.User;

@Database(entities = {User.class, Operation.class, OperationUser.class, Journal.class, MonthPeriod.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();

    public abstract OperationDao operationDao();

    public abstract OperationUserDao operationUserDao();

    public abstract JournalDao journalDao();
    public abstract PeriodDao periodDao();


    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "work_journal_db")
                            .addMigrations(Migrations.MIGRATION_1_2)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
