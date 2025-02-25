package com.app.workjournal.ui.report;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.workjournal.data.dto.JournalWithOperation;
import com.app.workjournal.data.repository.ReportRepository;

import java.util.Calendar;
import java.util.List;

public class ReportViewModel extends ViewModel {

    private final ReportRepository repository;
    private final MutableLiveData<String> mText;
    private final MutableLiveData<List<JournalWithOperation>> journal = new MutableLiveData<>();
    private final MutableLiveData<Integer> day = new MutableLiveData<>();
    private Calendar currentCalendar;

    public ReportViewModel(ReportRepository repository) {
        this.repository = repository;
        this.currentCalendar = Calendar.getInstance();
        this.mText = new MutableLiveData<>();
        mText.setValue("В цьому місяці ви не працювали");
        loadDayJournal(currentCalendar);
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<List<JournalWithOperation>> journalLiveData(Calendar calendar) {
        if (currentCalendar == null || !isSameDay(calendar, currentCalendar)) {
            currentCalendar = (Calendar) calendar.clone();
            loadDayJournal(currentCalendar);
        }
        return journal;
    }

    public LiveData<Integer> getWorkDayQuantity(int month, int year) {
        repository.getWorkDayQuantity(setDataCalendar(month, year), day);
        return day;
    }

    private boolean isSameDay(@NonNull Calendar cal1, @NonNull Calendar cal2) {
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }

    public void loadDayJournal(@NonNull Calendar calendar) {
            Calendar reportCalendar = setDataCalendar(calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR));
            repository.getWorkDayQuantity(reportCalendar, day);
            repository.report(reportCalendar, journal);
    }

    @SuppressLint("DefaultLocale")
    public String printReport() {
        List<JournalWithOperation> journals = journal.getValue();
        StringBuilder sb = new StringBuilder();
        if (journals != null && !journals.isEmpty()) {
            journals.forEach(operation -> {
                sb.append(String.format("%s - %d \n", operation.operationName(), operation.quantity()));
            });
        }
        return sb.toString();
    }


    @NonNull
    private Calendar setDataCalendar(int month, int year) {
        Calendar calendar = (Calendar) currentCalendar.clone();
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);
        return calendar;
    }
}
