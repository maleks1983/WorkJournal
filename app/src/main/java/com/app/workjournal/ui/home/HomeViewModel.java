package com.app.workjournal.ui.home;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.workjournal.data.dto.JournalWithOperation;
import com.app.workjournal.data.entity.Journal;
import com.app.workjournal.data.entity.Operation;
import com.app.workjournal.data.repository.HomeRepository;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HomeViewModel extends ViewModel {
    private final Calendar currentCalendar;
    private final HomeRepository repository;
    private final MutableLiveData<List<Operation>> listLiveOperationsUser = new MutableLiveData<>();
    private final MutableLiveData<Calendar> calendarLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<JournalWithOperation>> journalLive = new MutableLiveData<>();

    public HomeViewModel(@NonNull HomeRepository repository) {
        this.repository = repository;
        currentCalendar = Calendar.getInstance();
        calendarLiveData.setValue(currentCalendar);
    }

    public Calendar getCurrentCalendar() {
        setStartOfDay(currentCalendar);
        return currentCalendar;
    }

    // Get user journal based on the calendar
    LiveData<List<JournalWithOperation>> journalLiveData() {
        loadDayJournal();
        return journalLive;

    }

    LiveData<List<Operation>> operationsUserSelectedlivedata() {
        loadOperationsUser();
        return listLiveOperationsUser;
    }

    LiveData<Calendar> liveCalendar() {
        return calendarLiveData;
    }

    private void loadDayJournal() {
        repository.getJournalUserByDate(journalLive, getCurrentCalendar().getTimeInMillis());
    }

    private void loadOperationsUser() {
        repository.getUserOperationsSelected(listLiveOperationsUser);

    }


    void updateJournal(@NonNull Journal journal) {
        repository.updateJournal(journal);
        loadDayJournal();
    }

    public void deleteJournal(Journal journal) {
        repository.deleteJournal(journal);
        loadDayJournal();
    }


    public void buttonPreviousDate() {
        currentCalendar.add(Calendar.DAY_OF_MONTH, -1);
        calendarLiveData.setValue(currentCalendar);
        loadDayJournal();
    }

    public void buttonNextDate() {
        currentCalendar.add(Calendar.DAY_OF_MONTH, 1);
        calendarLiveData.setValue(currentCalendar);
        loadDayJournal();
    }

    public static void setStartOfDay(@NonNull Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }


    public void setCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        setStartOfDay(calendar);
        currentCalendar.setTimeInMillis(calendar.getTimeInMillis());
        calendarLiveData.setValue(currentCalendar);
        loadDayJournal();
    }
}
