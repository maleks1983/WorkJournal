package com.app.workjournal.ui.report;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.workjournal.R;
import com.app.workjournal.data.repository.ReportRepository;
import com.app.workjournal.databinding.FragmentReportBinding;
import com.google.android.material.snackbar.Snackbar;

import java.text.DateFormatSymbols;
import java.util.Calendar;

public class ReportFragment extends Fragment {
    private FragmentReportBinding binding;
    private final Calendar calendar;


    private ReportViewModel viewModel;

    public ReportFragment() {
        this.calendar = Calendar.getInstance();
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        ReportRepository repository = new ReportRepository(getContext());

        ReportViewModelFactory factory = new ReportViewModelFactory(repository);

        viewModel = new ViewModelProvider(this, factory).get(ReportViewModel.class);

        binding = FragmentReportBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        initCalendar();
        initJournalAdapter();

        viewModel.getWorkDayQuantity(
                binding.spinnerMonth.getSelectedItemPosition(),
                Integer.parseInt(binding.textViewYear.getText().toString()))
                .observe(getViewLifecycleOwner(), var -> {
            binding.reportWorkDay.setText(String.valueOf(var));

        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void initCalendar() {
        ArrayAdapter<String> monthAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                new DateFormatSymbols().getMonths()
        );
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        binding.spinnerMonth.setAdapter(monthAdapter);

        binding.spinnerMonth.setSelection(calendar.get(Calendar.MONTH));

        binding.spinnerMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                calendar.set(Calendar.MONTH, position);
                viewModel.loadDayJournal(calendar);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });

        binding.fab.setOnClickListener(view ->
                Snackbar.make(view, "Відправка місячного звіту", Snackbar.LENGTH_LONG)
                        .setAction("Відправити звіт", v -> {
                            sendReport(viewModel.printReport());
                        })
                        .setAnchorView(R.id.fab).show()
        );

        binding.textViewYear.setText(String.valueOf(calendar.get(Calendar.YEAR)));

        binding.calendarButtonUp.setOnClickListener(v -> {
            int year = calendar.get(Calendar.YEAR);
            int selectedMonth = binding.spinnerMonth.getSelectedItemPosition();


            calendar.set(Calendar.YEAR, ++year);
            calendar.set(Calendar.MONTH, selectedMonth);
            binding.textViewYear.setText(String.valueOf(calendar.get(Calendar.YEAR)));
            viewModel.loadDayJournal(calendar);
        });

        binding.calendarButtonDown.setOnClickListener(v -> {
            int year = calendar.get(Calendar.YEAR);
            int selectedMonth = binding.spinnerMonth.getSelectedItemPosition();

            calendar.set(Calendar.YEAR, --year);
            calendar.set(Calendar.MONTH, selectedMonth);
            binding.textViewYear.setText(String.valueOf(calendar.get(Calendar.YEAR)));
            viewModel.loadDayJournal(calendar);
        });
    }

    private void initJournalAdapter() {
        RecyclerView recyclerViewMonth = binding.recyclerViewReport;
        recyclerViewMonth.setLayoutManager(new LinearLayoutManager(requireContext()));
        viewModel.journalLiveData(calendar).observe(getViewLifecycleOwner(), journal -> {
            if (journal.isEmpty()) {
                binding.textReport.setText("В цьому місяці ви не працювали");
                binding.textReport.setVisibility(View.VISIBLE);
            } else {
                binding.textReport.setText("");
                binding.textReport.setVisibility(View.GONE);
            }
            JournalAdapterReport journalAdapter = new JournalAdapterReport(journal);
            recyclerViewMonth.setAdapter(journalAdapter);

        });
    }


    @SuppressLint("QueryPermissionsNeeded")
    private void sendReport(String report) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, report);

        if (intent.resolveActivity(requireContext().getPackageManager()) != null) {
            requireContext().startActivity(Intent.createChooser(intent, "Відправити через"));
        } else {
            Toast.makeText(getContext(), "Жоден застосунок не може обробити цей запит", Toast.LENGTH_SHORT).show();
        }
    }
}
