package com.app.workjournal.data.login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.app.workjournal.data.dto.LoggedInUser;
import com.app.workjournal.data.entity.User;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class LoginRepository {

    private static volatile LoginRepository instance;

    private final LoginDataSource dataSource;


    // If user credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore
    private LoggedInUser user = null;

    // private constructor : singleton access
    private LoginRepository(LoginDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static LoginRepository getInstance(LoginDataSource dataSource) {
        if (instance == null) {
            instance = new LoginRepository(dataSource);
        }
        return instance;
    }


    public boolean isLoggedIn() {
        if (this.user != null) {
            return true;
        } else {
            user = dataSource.getLoggedInUser();
            return this.user != null;
        }

    }

    public void logout() {

        user = null;
        dataSource.logout();
    }

    public void exit() {
        user = null;
        dataSource.exit();
    }

    private void setLoggedInUser(LoggedInUser user) {
        this.user = user;
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }

    public Result<LoggedInUser> login(String id, String username) {
        Result<LoggedInUser> result = dataSource.login(id, username);
        if (result instanceof Result.Success) {
            setLoggedInUser(((Result.Success<LoggedInUser>) result).getData());
        }
        return result;
    }


    public LoggedInUser getLoggedInUser() {
        return this.user;
    }

    public void updateUser(@NonNull User user) {
        dataSource.saveUserToPreferences(new LoggedInUser(user.getId().toString(), user.getName()));
    }

    @Nullable
    public static LoggedInUser getStaticLoggedInUser() {
        if (instance != null) {
            return instance.getLoggedInUser();
        } else {
            return null;
        }
    }
}