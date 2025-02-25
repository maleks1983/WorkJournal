package com.app.workjournal.ui.login;

/**
 * Class exposing authenticated user details to the UI.
 */
class LoggedInUserView {
    private String displayName;
    private String id;
    //... other data fields that may be accessible to the UI

    LoggedInUserView(String id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    String getDisplayName() {
        return displayName;
    }

    public String getId() {
        return id;
    }
}