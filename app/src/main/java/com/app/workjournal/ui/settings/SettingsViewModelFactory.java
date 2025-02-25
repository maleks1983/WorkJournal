package com.app.workjournal.ui.settings;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.app.workjournal.data.repository.SettingsRepository;
import com.app.workjournal.data.repository.UserRepository;

public class SettingsViewModelFactory implements ViewModelProvider.Factory {
    private final SettingsRepository repository;

    public SettingsViewModelFactory(SettingsRepository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(SettingsViewModel.class)) {
            return (T) new SettingsViewModel(repository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
