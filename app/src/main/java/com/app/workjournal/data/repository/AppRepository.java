package com.app.workjournal.data.repository;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.app.workjournal.data.db.AppDatabase;
import com.app.workjournal.data.db.CacheManager;
import com.app.workjournal.data.entity.Operation;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class AppRepository {
    private static volatile AppRepository instance;
    private final CacheManager cacheManager;
    private final AppDatabase database;
    private final ExecutorService executorService;


    public AppRepository(Context context) {
        this.database = AppDatabase.getInstance(context);
        this.cacheManager = CacheManager.getInstance();
        this.executorService = Executors.newSingleThreadExecutor();
        SharedPreferences sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        boolean registered = sharedPreferences.getBoolean("Registered", true);
        if (!registered) {
            initOperations();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("Registered");
            editor.apply();
        }

    }

    public static AppRepository getInstance(Context context) {
        if (instance == null) {
            instance = new AppRepository(context);
        }
        return instance;
    }


    private void initOperations() {
        List<String> operations = List.of(
                "Пломбування пристрою",
                "Тест плати, занесення плати в базу",
                "Прошивка робочою прошивкою",
                "Встановлення батарейки та фізичне тестування плати",
                "Складання паспортів",
                "Вкладання паспорта в пакет",
                "Збірка плати в корпус, наклейка наліпки",
                "Пакування плати в пакет 1 шт.",
                "Пакування плати в пакет по 5 шт.",
                "Пакування плати на відправку"
        );
        insertAllOperations(operations.stream()
                .map(Operation::new)
                .collect(Collectors.toSet()));


    }

    private void insertAllOperations(@NonNull Set<Operation> operations) {
        if (!operations.isEmpty()) {
            executorService.execute(() -> {
                try {
                    database.operationDao().insertAll(List.copyOf(operations));
                    cacheManager.putOperations(database.operationDao().getAllOperations());
                } catch (Exception e) {
                    Log.e("DatabaseError", "Error AppRepository insert all operations", e);
                }
            });
        }
    }

}
