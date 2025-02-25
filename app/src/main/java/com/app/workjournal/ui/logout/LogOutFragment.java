package com.app.workjournal.ui.logout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.app.workjournal.R;
import com.app.workjournal.data.login.LoginDataSource;
import com.app.workjournal.data.login.LoginRepository;
import com.app.workjournal.databinding.FragmentLogoutBinding;

public class LogOutFragment extends Fragment {

    private FragmentLogoutBinding binding;
    private LoginRepository repository;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentLogoutBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        initButton();
        repository = LoginRepository.getInstance(new LoginDataSource(getContext()));
        return root;
    }

    private void initButton() {
        binding.buttonYes.setOnClickListener(v -> {
            repository.exit();
            requireActivity().finish();
        });
        binding.buttonNo.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
            navController.navigate(R.id.nav_home);
        });
    }

}
