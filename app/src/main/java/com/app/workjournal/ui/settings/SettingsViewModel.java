package com.app.workjournal.ui.settings;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.app.workjournal.data.dto.SelectedOperation;
import com.app.workjournal.data.entity.Operation;
import com.app.workjournal.data.entity.User;
import com.app.workjournal.data.repository.SettingsRepository;

import java.util.List;
import java.util.Set;

public class SettingsViewModel extends ViewModel {
    private final MediatorLiveData<User> userMutableLiveData = new MediatorLiveData<>();
    private final MediatorLiveData<List<SelectedOperation>> listLiveData = new MediatorLiveData<>();
    private final SettingsRepository repository;


    public SettingsViewModel(@NonNull SettingsRepository repository) {
        this.repository = repository;
        userMutableLiveData.addSource(repository.getUserLive(), userMutableLiveData::setValue);
    }


    public LiveData<User> getUserMutableLiveData() {
        return userMutableLiveData;
    }

    public LiveData<List<SelectedOperation>> getSelectedOperations() {
        updateOperationsList();
        return listLiveData;
    }

    public void saveUserOperations(Set<SelectedOperation> selectedOperations) {
        repository.insertAllUserOperationsSettings(selectedOperations);
    }

    public void updateUser(User user) {
        repository.updateUser(user);
    }

    public void updateOperationsList() {
        repository.getOperationsSettings(listLiveData);
    }

    public void addNewOperations(String name) {
        Operation newOperations = new Operation(name);
        repository.addOperations(newOperations);
    }

    public void updateOperation(Operation operation) {
        repository.updateOperation(operation);
        updateOperationsList();
    }

    public void deleteOperation(Operation operation) {
        repository.deleteOperations(operation);
        updateOperationsList();
    }

}


