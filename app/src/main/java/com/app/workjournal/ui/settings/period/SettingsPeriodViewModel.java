package com.app.workjournal.ui.settings.period;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.app.workjournal.data.entity.MonthPeriod;
import com.app.workjournal.data.repository.PeriodRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SettingsPeriodViewModel extends ViewModel {
    private final PeriodRepository repository;

    public SettingsPeriodViewModel(PeriodRepository repository) {
        this.repository = repository;
    }

    public LiveData<MonthPeriod> periodLiveData(int id) {
        return repository.getMonthPeriod(id);
    }

    public void updatePeriod(int id, String startTime, String endTime) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy", Locale.getDefault());
            Calendar startPeriod = Calendar.getInstance();
            Calendar endPeriod = Calendar.getInstance();
            Date date = sdf.parse(startTime);
            if (date != null) {
                startPeriod.setTime(date);
            }
            date = sdf.parse(endTime);
            if (date != null) {
                endPeriod.setTime(date);
            }
            repository.updatePeriod(new MonthPeriod(id, startPeriod.getTimeInMillis(), endPeriod.getTimeInMillis()));
        } catch (ParseException e) {
            e.fillInStackTrace();
        }


    }


}


