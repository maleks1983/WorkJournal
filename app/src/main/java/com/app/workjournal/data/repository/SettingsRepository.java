package com.app.workjournal.data.repository;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.app.workjournal.data.db.AppDatabase;
import com.app.workjournal.data.db.CacheManager;
import com.app.workjournal.data.dto.SelectedOperation;
import com.app.workjournal.data.entity.Operation;
import com.app.workjournal.data.entity.OperationUser;
import com.app.workjournal.data.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class SettingsRepository {
    private static volatile SettingsRepository instance;

    private final CacheManager cacheManager;
    private final ExecutorService executorService;
    private final AppDatabase database;
    private final User user;
    private final UserRepository userRepository;



    public SettingsRepository(Context context) {
        cacheManager = CacheManager.getInstance();
        executorService = Executors.newSingleThreadExecutor();
        database = AppDatabase.getInstance(context);
        userRepository = UserRepository.getInstance(context);
        user = Objects.requireNonNull(userRepository.getUser());
    }

    public static SettingsRepository getInstance(Context context) {
        if (instance == null) {
            instance = new SettingsRepository(context);
        }
        return instance;
    }


    public LiveData<User> getUserLive() {
        return database.userDao().getUserLiveData(user.getId());
    }

    public void updateUser(@NonNull User user) {
        executorService.execute(() -> {
            try {
                userRepository.update(user);
            } catch (Exception e) {
                Log.e("DatabaseError", "Error repository update user", e);
            }
        });
    }

    public void insertAllUserOperationsSettings(@NonNull Set<SelectedOperation> operations) {
        executorService.execute(() -> {
            try {
                List<OperationUser> userOperations = operations.stream()
                        .map(operation -> new OperationUser(user.getId(), operation.getOperation().getId()))
                        .collect(Collectors.toList());
                database.operationUserDao().deleteAll(user.getId());
                database.operationUserDao().insertAll(userOperations);
                List<SelectedOperation> operationSelected = cacheManager.getOperations().stream()
                        .map(operation -> {
                            Set<OperationUser> b = userOperations.stream()
                                    .filter(operationUser -> {
                                        return operationUser.getOperationId().equals(operation.getId());
                                    }).collect(Collectors.toSet());
                            return new SelectedOperation(operation, !b.isEmpty());
                        }).collect(Collectors.toList());
                cacheManager.putOperationsHome(database.operationDao().getOperationsByUserId(user.getId()));
                cacheManager.putSelectedOperationsSettings(operationSelected);
            } catch (Exception e) {
                Log.e("DatabaseError", "Error inserting operations", e);
            }
        });

    }

    public void getOperationsSettings(MediatorLiveData<List<SelectedOperation>> operationsMediatorLiveData) {
        executorService.submit(() -> {
            try {
                List<SelectedOperation> operationSelected = cacheManager.getSelectedOperationsSettings();
                if (operationSelected == null) {
                    List<Operation> operations = cacheManager.getOperations();
                    if (Objects.isNull(operations)) {
                        operations = database.operationDao().getAllOperations();
                        cacheManager.putOperations(operations);
                    }
                    List<OperationUser> finalOperationsUser = database.operationUserDao().getAll(user.getId());
                    operationSelected = operations.stream()
                            .map(operation -> {
                                Set<OperationUser> b = finalOperationsUser.stream()
                                        .filter(operationUser -> {
                                            return operationUser.getOperationId().equals(operation.getId());
                                        }).collect(Collectors.toSet());
                                return new SelectedOperation(operation, !b.isEmpty());
                            }).collect(Collectors.toList());
                    cacheManager.putSelectedOperationsSettings(operationSelected);
                }
                operationsMediatorLiveData.postValue(operationSelected);
            } catch (Exception e) {
                Log.e("DatabaseError", "Error getOperationsSelected()", e);
                operationsMediatorLiveData.postValue(new ArrayList<>());
            }

        });
    }

    public void addOperations(Operation newOperations) {
        executorService.execute(() -> {
            try {
                database.operationDao().insert(newOperations);
                removeCacheSelectedOperations();
            } catch (Exception e) {
                Log.e("DatabaseError", "Error addOperations()", e);
            }
        });
    }

    public void deleteOperations(Operation operations) {
        executorService.execute(() -> {
            try {
                database.operationDao().delete(operations);
                removeCacheSelectedOperations();
            } catch (Exception e) {
                Log.e("DatabaseError", "Error deleteOperations()", e);
            }
        });
    }

    private void removeCacheSelectedOperations() {
        cacheManager.removeSelectedOperationsSettings();
        cacheManager.removeOperations();
    }

    public void updateOperation(Operation operation) {
        executorService.execute(() -> {
            try {
                database.operationDao().update(operation);
                removeCacheSelectedOperations();
            } catch (Exception e) {
                Log.e("DatabaseError", "Error updateOperation()", e);
            }
        });

    }

}
