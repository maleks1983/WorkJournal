package com.app.workjournal.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.workjournal.R;
import com.app.workjournal.data.dto.SelectedOperation;
import com.app.workjournal.data.entity.User;
import com.app.workjournal.data.repository.SettingsRepository;
import com.app.workjournal.databinding.FragmentSettingsBinding;
import com.app.workjournal.ui.settings.period.SettingsDialogPeriodFragment;

import java.util.Set;
import java.util.stream.Collectors;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;
    private SettingsViewModel viewModel;
    private User user;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        SettingsRepository repository = new SettingsRepository(getContext());
        SettingsViewModelFactory factory = new SettingsViewModelFactory(repository);


        viewModel = new ViewModelProvider(this, factory).get(SettingsViewModel.class);


        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        viewModel.getUserMutableLiveData().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                this.user = user;
                binding.textNameUser.setText(user.getName());
                binding.editTexIdUser.setText(String.valueOf(user.getId()));
            }
        });


        viewModel.getSelectedOperations().observe(getViewLifecycleOwner(), operations -> {
            if (operations != null) {
                OperationSelectedAdapter operationSelectedAdapter = new OperationSelectedAdapter(viewModel, operations,requireActivity().getSupportFragmentManager());
                RecyclerView recyclerView = binding.recyclerViewOperations;
                recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
                recyclerView.setAdapter(operationSelectedAdapter);


                binding.buttonSave.setOnClickListener(v -> {

                    Set<SelectedOperation> selectedOperations = operations.stream()
                            .filter(operation -> Boolean.TRUE.equals(operation.getSelected()))
                            .collect(Collectors.toSet());


                    viewModel.saveUserOperations(selectedOperations);
                    this.user.setName(binding.textNameUser.getText().toString());
                    viewModel.updateUser(user);
                    NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
                    navController.navigate(R.id.nav_home);

                });
            }
        });
        requireActivity().addMenuProvider(new MenuProvider() {

            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.main, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.action_settings_add) {
                    new SettingsDialogAddOperationFragment(viewModel).show(getParentFragmentManager(), "Dialog");
                    Toast.makeText(requireContext(), "Налаштування натиснуті", Toast.LENGTH_SHORT).show();
                    return true;
                }
                if (menuItem.getItemId() == R.id.action_settings_update_period) {
                    new SettingsDialogPeriodFragment().show(getParentFragmentManager(), "Dialog");
                    Toast.makeText(requireContext(), "Зміна періоду натиснута", Toast.LENGTH_SHORT).show();
                    return true;
                }

                return false;

            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}
