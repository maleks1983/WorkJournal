package com.app.workjournal.data.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.app.workjournal.data.dto.JournalWithOperation;

@Entity(tableName = "journal",
        foreignKeys = {
                @ForeignKey(
                        entity = Operation.class,
                        parentColumns = "id",
                        childColumns = "operationId",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(
                        entity = User.class,
                        parentColumns = "id",
                        childColumns = "userId",
                        onDelete = ForeignKey.CASCADE)
        },
        indices = {
                @Index(value = "userId"),
                @Index(value = "operationId")
        }

)
public class Journal {
    @PrimaryKey(autoGenerate = true)
    private Integer id;
    private Long date;
    private Long userId; // Foreign Key для User
    private Integer operationId; // Foreign Key для Operation
    private int quantity;

    @Ignore
    public Journal(Integer id, Long date, Integer operationId, int quantity) {
        this.id = id;
        this.date = date;
        this.operationId = operationId;
        this.quantity = quantity;
    }

    @Ignore
    public Journal(Long date, Integer operationId, int quantity) {
        this.date = date;
        this.operationId = operationId;
        this.quantity = quantity;
    }

    public Journal(Long date, Long userId, Integer operationId, int quantity) {
        this.date = date;
        this.userId = userId;
        this.operationId = operationId;
        this.quantity = quantity;
    }

    @Ignore
    public Journal(@NonNull JournalWithOperation journalWithOperation) {
        this.id = journalWithOperation.id();
        this.date = journalWithOperation.date();
        this.operationId = journalWithOperation.operationId();
        this.quantity = journalWithOperation.quantity();

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getOperationId() {
        return operationId;
    }

    public void setOperationId(Integer operationId) {
        this.operationId = operationId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isNew() {
        return this.id == null;
    }
}
