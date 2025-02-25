package com.app.workjournal.data.repository;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.app.workjournal.data.db.AppDatabase;
import com.app.workjournal.data.db.CacheManager;
import com.app.workjournal.data.dto.LoggedInUser;
import com.app.workjournal.data.entity.User;
import com.app.workjournal.data.login.LoginRepository;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserRepository {
    @SuppressLint("StaticFieldLeak")
    private static UserRepository instance;
    private final AppDatabase database;
    private final ExecutorService executorService;
    private final CacheManager cacheManager;
    private User user;


    public UserRepository(Context context) {
        this.database = AppDatabase.getInstance(context);
        this.cacheManager = CacheManager.getInstance();
        executorService = Executors.newSingleThreadExecutor();

    }

    public static synchronized UserRepository getInstance(Context context) {
        if (instance == null) {
            instance = new UserRepository(context);
        }
        return instance;
    }


    @Nullable
    public User getUser() {
        if (user == null) {
            user = getCacheUser();
            if (user == null) {
                setUser();
            }
        }
        return user;
    }

    @Nullable
    private User getCacheUser() {
        try {
            return cacheManager.getUser();
        } catch (Exception e) {
            Log.e("DatabaseError", "Error UserRepository getCacheUser()", e);
            return null;
        }
    }

    public void putCacheUser(User user) {
        try {
            cacheManager.putUser(user);
        } catch (Exception e) {
            Log.e("DatabaseError", "Error UserRepository putCacheUser()", e);
        }
    }


    private void loadUser(Long userId) {
        try {
            user = database.userDao().get(userId);
        } catch (Exception e) {
            Log.e("DatabaseError", "Error UserRepository getUser()", e);
        }
    }

    private void insertUser(User user) {
        try {
            database.userDao().insert(user);
        } catch (Exception e) {
            Log.e("DatabaseError", "Error UserRepository insertUser()", e);
        }
    }


    private void setUser() {
        try {
            executorService.submit(() -> {
                LoggedInUser loggedInUser = LoginRepository.getStaticLoggedInUser();
                if (loggedInUser != null) {
                    loadUser(Long.decode(loggedInUser.getUserId()));
                    if (user == null) {
                        user = new User(loggedInUser);
                        insertUser(user);
                        loadUser(Long.decode(loggedInUser.getUserId()));
                        putCacheUser(user);
                    }
                }
            }).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void update(User user) {
        try{
            executorService.execute(() -> {
                database.userDao().update(user);
                putCacheUser(user);
            });
        } catch (Exception e) {
            Log.e("DatabaseError", "Error UserRepository update()", e);
        }

    }


}


