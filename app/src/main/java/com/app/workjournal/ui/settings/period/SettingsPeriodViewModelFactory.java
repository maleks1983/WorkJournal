package com.app.workjournal.ui.settings.period;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.app.workjournal.data.repository.PeriodRepository;
import com.app.workjournal.data.repository.SettingsRepository;
import com.app.workjournal.ui.settings.SettingsViewModel;

public class SettingsPeriodViewModelFactory implements ViewModelProvider.Factory {
    private final PeriodRepository repository;

    public SettingsPeriodViewModelFactory(PeriodRepository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(SettingsPeriodViewModel.class)) {
            return (T) new SettingsPeriodViewModel(repository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
