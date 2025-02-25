package com.app.workjournal.data.dto;

public record JournalWithOperation(
        Integer id,
        long date,
        Long userId,
        int quantity,
        Integer operationId,
        String operationName) {
    @Override
    public Integer id() {
        return id;
    }

    @Override
    public long date() {
        return date;
    }

    @Override
    public Long userId() {
        return userId;
    }

    @Override
    public int quantity() {
        return quantity;
    }

    @Override
    public Integer operationId() {
        return operationId;
    }

    @Override
    public String operationName() {
        return operationName;
    }
}