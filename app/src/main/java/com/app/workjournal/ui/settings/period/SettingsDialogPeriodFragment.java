package com.app.workjournal.ui.settings.period;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.app.workjournal.R;
import com.app.workjournal.data.entity.MonthPeriod;
import com.app.workjournal.data.repository.PeriodRepository;
import com.app.workjournal.databinding.DialogMonthPeriodLayoutBinding;
import com.app.workjournal.ui.home.CalendarDialogFragment;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SettingsDialogPeriodFragment extends DialogFragment {
    private final Calendar calendar;
    private DialogMonthPeriodLayoutBinding binding;
    private SettingsPeriodViewModel viewModel;


    public SettingsDialogPeriodFragment() {
        this.calendar = Calendar.getInstance();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogMonthPeriodLayoutBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        PeriodRepository repository = new PeriodRepository(getContext());
        SettingsPeriodViewModelFactory factory = new SettingsPeriodViewModelFactory(repository);
        viewModel = new ViewModelProvider(this, factory).get(SettingsPeriodViewModel.class);

        initCalendar();

        binding.textViewYear.setText(String.valueOf(calendar.get(Calendar.YEAR)));
        binding.monthPeriodStartTime.setOnClickListener(this::showCalendar);
        binding.monthPeriodEndTime.setOnClickListener(this::showCalendar);
        Button button = view.findViewById(R.id.add_operations_btn_ok);
        button.setOnClickListener(v -> {
            viewModel.updatePeriod(
                    Integer.parseInt(String.format(Locale.UK, "%s%02d", binding.textViewYear.getText(), calendar.get(Calendar.MONTH) + 1)),
                    binding.monthPeriodStartTime.getText().toString(),
                    binding.monthPeriodEndTime.getText().toString());
            dismiss();
        });
        binding.calendarButtonCancel.setOnClickListener(v -> {
            dismiss();
        });

        binding.calendarButtonUp.setOnClickListener(v -> {
            int year = calendar.get(Calendar.YEAR);
            year++;
            calendar.set(Calendar.YEAR, year);
            binding.textViewYear.setText(String.valueOf(calendar.get(Calendar.YEAR)));
            renderPeriod(binding.spinnerMonthPeriod.getSelectedItemPosition());
        });
        binding.calendarButtonDown.setOnClickListener(v -> {
            int year = calendar.get(Calendar.YEAR);
            year--;
            calendar.set(Calendar.YEAR, year);
            binding.textViewYear.setText(String.valueOf(calendar.get(Calendar.YEAR)));
            renderPeriod(binding.spinnerMonthPeriod.getSelectedItemPosition());
        });

        return view;
    }

    private void initCalendar() {
        ArrayAdapter<String> monthAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                new DateFormatSymbols().getMonths()
        );

        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        binding.spinnerMonthPeriod.setAdapter(monthAdapter);

        binding.spinnerMonthPeriod.setSelection(calendar.get(Calendar.MONTH));
        monthAdapter.notifyDataSetChanged();

        binding.spinnerMonthPeriod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                renderPeriod(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    private void showCalendar(View view) {
        CalendarDialogFragment calendarDialog = new CalendarDialogFragment();
        calendarDialog.setOnDateSelectedListener(date -> {
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy", Locale.getDefault());
            String formattedDate = sdf.format(new Date(date));
            ((TextView) view).setText(formattedDate);
            Toast.makeText(requireContext(), "Вибрано: " + formattedDate, Toast.LENGTH_SHORT).show();
        });
        calendarDialog.show(getParentFragmentManager(), "calendarDialog");
    }

    private void renderPeriod(int position) {
        int year = Integer.parseInt(binding.textViewYear.getText().toString());
        int month = position + 1;

        calendar.set(Calendar.MONTH, position);
        calendar.set(Calendar.YEAR, year);

        int periodId = Integer.parseInt(String.format(Locale.UK, "%d%02d", year, month));

        viewModel.periodLiveData(periodId).observe(getViewLifecycleOwner(), period -> {
            if (period == null) {
                period = new MonthPeriod(year, month);
            }

            Calendar startPeriod = Calendar.getInstance();
            startPeriod.setTimeInMillis(period.getStartPeriod());
            String startTime = String.format(Locale.UK, "%02d.%02d.%s",
                    startPeriod.get(Calendar.DATE),
                    startPeriod.get(Calendar.MONTH) + 1,
                    startPeriod.get(Calendar.YEAR) % 100);
            binding.monthPeriodStartTime.setText(startTime);

            Calendar endPeriod = Calendar.getInstance();
            endPeriod.setTimeInMillis(period.getEndPeriod());
            String endTime = String.format(Locale.UK, "%02d.%02d.%s",
                    endPeriod.get(Calendar.DATE),
                    endPeriod.get(Calendar.MONTH) + 1,
                    endPeriod.get(Calendar.YEAR) % 100);
            binding.monthPeriodEndTime.setText(endTime);
        });

    }

}
