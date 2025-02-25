package com.app.workjournal.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.app.workjournal.data.dto.LoggedInUser;


@Entity(tableName = "users")
public class User {
    @PrimaryKey
    private final Long id;
    private String name;

    public User(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public User(LoggedInUser user) {
        this.name = user.getDisplayName();
        this.id = Long.decode(user.getUserId());
    }

    public Long getId() {
        return id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
