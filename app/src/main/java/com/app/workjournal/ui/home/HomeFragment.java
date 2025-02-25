package com.app.workjournal.ui.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.workjournal.R;
import com.app.workjournal.data.repository.HomeRepository;
import com.app.workjournal.databinding.FragmentHomeBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class HomeFragment extends Fragment {
    private TextView textCurrentDate;

    private FragmentHomeBinding binding;

    private HomeViewModel viewModel;


    @SuppressLint("ClickableViewAccessibility")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        HomeRepository repository = new HomeRepository(getContext());

        HomeViewModelFactory factory = new HomeViewModelFactory(repository);

        viewModel = new ViewModelProvider(this, factory).get(HomeViewModel.class);


        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        RecyclerView recyclerViewDay = binding.recyclerViewJournalsDay;


        recyclerViewDay.setLayoutManager(new LinearLayoutManager(requireContext()));


        RecyclerView recyclerViewMonth = binding.recyclerViewJournalsMonth;
        recyclerViewMonth.setLayoutManager(new LinearLayoutManager(requireContext()));



        viewModel.journalLiveData().observe(getViewLifecycleOwner(), journal -> {
            JournalAdapter journalAdapter = new JournalAdapter(journal, viewModel, getContext());
            recyclerViewMonth.setAdapter(journalAdapter);


        });

        binding.expandArrow.setOnClickListener(v -> {
            if (binding.recyclerViewJournalsDay.getVisibility() == View.GONE) {
                openListOperation();
            } else {
                closeListOperation();
            }

        });
        initCalendar(root);
        return root;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerViewDay = binding.recyclerViewJournalsDay;

        OperationsUserAdapter operationAdapter = new OperationsUserAdapter(new ArrayList<>(), viewModel, getContext());
        recyclerViewDay.setAdapter(operationAdapter);

        viewModel.operationsUserSelectedlivedata().observe(getViewLifecycleOwner(), operations -> {
            if (operations != null && !operations.isEmpty()) {
                recyclerViewDay.post(this::setHeightOperationsLayout);
                operationAdapter.updateData(operations);
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void initCalendar(@NonNull View root) {

        textCurrentDate = root.findViewById(R.id.text_current_date);
        textCurrentDate.setOnClickListener(v -> {
            showCalendar();
        });

        Button buttonPreviousDate = root.findViewById(R.id.button_previous_date);
        Button buttonNextDate = root.findViewById(R.id.button_next_date);

        viewModel.liveCalendar().observe(getViewLifecycleOwner(), this::updateDate);

        buttonPreviousDate.setOnClickListener(v -> {
            viewModel.buttonPreviousDate();

        });

        buttonNextDate.setOnClickListener(v -> {
            viewModel.buttonNextDate();
        });

    }

    private void updateDate(@NonNull Calendar calendar) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        textCurrentDate.setText(dateFormat.format(calendar.getTime()));
        if (dateFormat.format(calendar.getTime()).equals(dateFormat.format(Calendar.getInstance().getTime()))) {
            openListOperation();
        } else {
            closeListOperation();
        }

    }

    private void openListOperation() {
        binding.recyclerViewJournalsDay.setVisibility(View.VISIBLE);
        binding.expandArrow.setRotation(180);
        setHeightOperationsLayout();
    }

    private void setHeightOperationsLayout() {
        int heightOperation = binding.recyclerViewJournalsDay.getHeight();
        int heightHome = binding.getRoot().getHeight();
        if (heightOperation > heightHome / 2) {
            ViewGroup.LayoutParams params = binding.recyclerViewJournalsDay.getLayoutParams();
            params.height = heightHome / 2;
            binding.recyclerViewJournalsDay.setLayoutParams(params);
        }
    }

    private void closeListOperation() {
        binding.recyclerViewJournalsDay.setVisibility(View.GONE);
        binding.expandArrow.setRotation(0);
    }

    private void showCalendar() {
        CalendarDialogFragment calendarDialog = new CalendarDialogFragment();

        calendarDialog.setOnDateSelectedListener(date -> {
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
            String formattedDate = sdf.format(new Date(date));
            viewModel.setCalendar(new Date(date));
            Toast.makeText(requireContext(), "Вибрано: " + formattedDate, Toast.LENGTH_SHORT).show();
        });

        calendarDialog.show(getParentFragmentManager(), "calendarDialog");

    }

}