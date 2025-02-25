package com.app.workjournal.ui.login;

import androidx.annotation.Nullable;

/**
 * Data validation state of the login form.
 */
class LoginFormState {
    @Nullable
    private Integer usernameError;
    @Nullable
    private Integer telephoneError;
    private boolean isDataValid;

    LoginFormState(@Nullable Integer usernameError, @Nullable Integer telephoneError) {
        this.usernameError = usernameError;
        this.telephoneError = telephoneError;
        this.isDataValid = false;
    }

    LoginFormState(boolean isDataValid) {
        this.usernameError = null;
        this.telephoneError = null;
        this.isDataValid = isDataValid;
    }

    @Nullable
    Integer getUsernameError() {
        return usernameError;
    }

    @Nullable
    Integer getTelephoneError() {
        return telephoneError;
    }

    boolean isDataValid() {
        return isDataValid;
    }
}