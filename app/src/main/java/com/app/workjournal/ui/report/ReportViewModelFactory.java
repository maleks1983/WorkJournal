package com.app.workjournal.ui.report;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.app.workjournal.data.repository.ReportRepository;
import com.app.workjournal.data.repository.UserRepository;

public class ReportViewModelFactory implements ViewModelProvider.Factory {
    private final ReportRepository repository;

    public ReportViewModelFactory(ReportRepository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ReportViewModel.class)) {
            return (T) new ReportViewModel(repository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
