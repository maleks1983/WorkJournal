package com.app.workjournal.data.dto;

import com.app.workjournal.data.entity.Operation;

public class SelectedOperation {
    private Operation operation;
    private Boolean selected;

    public SelectedOperation(Operation operation, boolean contains) {
        this.operation = operation;
        this.selected = contains;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }
}
