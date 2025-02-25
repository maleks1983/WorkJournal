package com.app.workjournal.data.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.app.workjournal.data.dto.LoggedInUser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    private final Context context;

    public LoginDataSource(Context context) {
        this.context = context;
    }


    public Result<LoggedInUser> login(String id, String username) {

        try {
            LoggedInUser user = getLoggedInUser();
            if (user != null) {
                if (user.getUserId().equals(id) && user.getDisplayName().equals(username)) {
                    LoggedInUser fakeUser = new LoggedInUser(id, username);
                    return new Result.Success<>(fakeUser);
                }
            } else {
                LoggedInUser fakeUser = new LoggedInUser(id, username);
                saveUserToPreferences(fakeUser);
                return new Result.Success<>(fakeUser);
            }

        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
        return null;
    }

    public void logout() {
        clearSharedPreferences();
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
    }
    public void exit() {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
    }

    public LoggedInUser getLoggedInUser() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String json = sharedPreferences.getString("LoggedInUser", null);
        if (json != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<LoggedInUser>() {
            }.getType();
            return gson.fromJson(json, type);
        } else return null;
    }

    public void clearSharedPreferences() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public void saveUserToPreferences(LoggedInUser user) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        try {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            Gson gson = new Gson();
            String json = gson.toJson(user);
            editor.putString("LoggedInUser", json);
            editor.putBoolean("Registered", false);
            editor.apply();
        } catch (Exception e) {
            Log.e("saveUserToPreferences", e.toString());
        }
    }
}