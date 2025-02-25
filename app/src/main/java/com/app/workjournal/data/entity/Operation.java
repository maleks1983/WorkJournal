package com.app.workjournal.data.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "operations",
        indices = {
                @Index(value = "nameOperation", unique = true)})
public class Operation {
    @PrimaryKey(autoGenerate = true)
    private Integer id;
    private String nameOperation;


    public Operation(Integer id, String nameOperation) {
        this.id = id;
        this.nameOperation = nameOperation;
    }

    @Ignore
    public Operation(String nameOperation) {
        this.nameOperation = nameOperation;
    }

    @Ignore
    public Operation(@NonNull Operation operation) {
        this.id = operation.getId();
        this.nameOperation = operation.getNameOperation();
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return this.id;
    }

    public String getNameOperation() {
        return nameOperation;
    }

    public void setNameOperation(String nameOperation) {
        this.nameOperation = nameOperation;
    }


}
