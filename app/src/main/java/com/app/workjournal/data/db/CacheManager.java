package com.app.workjournal.data.db;

import android.annotation.SuppressLint;
import android.util.LruCache;

import com.app.workjournal.data.dto.JournalWithOperation;
import com.app.workjournal.data.dto.SelectedOperation;
import com.app.workjournal.data.entity.Operation;
import com.app.workjournal.data.entity.OperationUser;
import com.app.workjournal.data.entity.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CacheManager {
    @SuppressLint("StaticFieldLeak")
    private static CacheManager instance;

    private final LruCache<String, User> cacheUser;
    private final LruCache<String, List<Operation>> cacheOperation;
    private final LruCache<String, List<Operation>> cacheOperationHome;
    private final LruCache<String, List<SelectedOperation>> cacheSettingsOperations;

    private final LruCache<String, Map<Long, List<JournalWithOperation>>> cacheJournal;


    public static synchronized CacheManager getInstance() {
        if (instance == null) {
            instance = new CacheManager();
        }
        return instance;
    }

    private CacheManager() {
        int cacheSize = 4 * 1024 * 1024;
        cacheUser = new LruCache<>(cacheSize);
        cacheSettingsOperations = new LruCache<>(cacheSize);
        cacheJournal = new LruCache<>(cacheSize);
        cacheOperation = new LruCache<>(cacheSize);
        cacheOperationHome = new LruCache<>(cacheSize);
    }

    public void putUser(User user) {
        cacheUser.put("user", user);
    }

    public User getUser() {
        return cacheUser.get("user");
    }

    public void putOperations(List<Operation> operations) {
        cacheOperation.remove("list_operations_live");
        cacheOperation.put("list_operations_live", operations);
    }

    public void removeOperations() {
        cacheOperation.remove("list_operations_live");
    }

    public List<Operation> getOperations() {
        return cacheOperation.get("list_operations_live");
    }

    public void putOperationsHome(List<Operation> operations) {
        cacheOperationHome.remove("list_operations_home_live");
        cacheOperationHome.put("list_operations_home_live", operations);
    }

    public List<Operation> getOperationsHome() {
        return cacheOperationHome.get("list_operations_home_live");
    }

    public void putSelectedOperationsSettings(List<SelectedOperation> operations) {
        cacheSettingsOperations.remove("list_operations_selected_live");
        cacheSettingsOperations.put("list_operations_selected_live", operations);
    }

    public List<SelectedOperation> getSelectedOperationsSettings() {
        return cacheSettingsOperations.get("list_operations_selected_live");
    }

    public void removeSelectedOperationsSettings() {
        cacheSettingsOperations.remove("list_operations_selected_live");
    }

    public void putJournal(Long day, List<JournalWithOperation> journal) {
        Map<Long, List<JournalWithOperation>> m = cacheJournal.get("list_journal_live");
        if (m == null) {
            Map<Long, List<JournalWithOperation>> map = new HashMap<>();
            map.put(day, journal);
            cacheJournal.put("list_journal_live", map);
        }
        cacheJournal.get("list_journal_live").put(day, journal);
    }

    public List<JournalWithOperation> getJournal(Long day) {
        Map<Long, List<JournalWithOperation>> m = cacheJournal.get("list_journal_live");
        if (m == null) {
            return null;
        }
        return m.get(day);
    }
}
