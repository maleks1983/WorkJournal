package com.app.workjournal.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.util.Patterns;

import com.app.workjournal.data.login.LoginRepository;
import com.app.workjournal.data.login.Result;
import com.app.workjournal.data.dto.LoggedInUser;
import com.app.workjournal.R;

public class LoginViewModel extends ViewModel {
    private LoginRepository loginRepository;

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private MutableLiveData<String> liveUserName = new MutableLiveData<>();

    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }
    public LiveData<String>getLiveUserName(){
        liveUserName.postValue(loginRepository.getLoggedInUser().getDisplayName());
        return liveUserName;
    }

    public void login(String telephone, String username) {
        // can be launched in a separate asynchronous job
        Result<LoggedInUser> result = loginRepository.login(telephone, username);

        if (result instanceof Result.Success) {
            LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
            loginResult.setValue(new LoginResult(new LoggedInUserView(data.getUserId(), data.getDisplayName())));

        } else {
            loginResult.setValue(new LoginResult(R.string.login_failed));
        }
    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }

    public boolean isLoggedIn() {
        return loginRepository.isLoggedIn();
    }

    public LoggedInUser getLoggedInUser() {
        return loginRepository.getLoggedInUser();
    }
}