package com.app.workjournal.data.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

@Entity(tableName = "operations_user",
        primaryKeys = {"userId", "operationId"},
        foreignKeys = {
                @ForeignKey(
                        entity = User.class,
                        parentColumns = "id",
                        childColumns = "userId",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(
                        entity = Operation.class,
                        parentColumns = "id",
                        childColumns = "operationId",
                        onDelete = ForeignKey.CASCADE)
        }, indices = {
        @Index(value = "userId"),
        @Index(value = "operationId")
}
)
public class OperationUser {
    @NonNull
    private Long userId;
    @NonNull
    private Integer operationId;


    public OperationUser(@NonNull Long userId, Integer operationId) {
        this.userId = userId;
        this.operationId = operationId;
    }

    @NonNull
    public Long getUserId() {
        return userId;
    }


    public void setUserId(@NonNull Long userId) {
        this.userId = userId;
    }

    @NonNull
    public Integer getOperationId() {
        return operationId;
    }

    public void setOperationId(Integer operationId) {
        this.operationId = operationId;
    }
}
