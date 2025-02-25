package com.app.workjournal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.app.workjournal.data.repository.AppRepository;
import com.app.workjournal.data.repository.SettingsRepository;
import com.app.workjournal.databinding.ActivityMainBinding;
import com.app.workjournal.ui.login.LoginActivity;
import com.app.workjournal.ui.login.LoginViewModel;
import com.app.workjournal.ui.login.LoginViewModelFactory;
import com.app.workjournal.ui.settings.SettingsViewModel;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LoginViewModel loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory(this))
                .get(LoginViewModel.class);

        if (!loginViewModel.isLoggedIn()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            AppRepository appRepository = AppRepository.getInstance(getBaseContext());
            SettingsViewModel viewModel = new SettingsViewModel(SettingsRepository.getInstance(this));
            binding = ActivityMainBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());
            setSupportActionBar(binding.appBarMain.toolbar);

            DrawerLayout drawer = binding.drawerLayout;
            mAppBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.nav_home, R.id.nav_settings, R.id.nav_report, R.id.nav_logout)
                    .setOpenableLayout(drawer)
                    .build();
            NavigationView navigationView = binding.navView;
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
            NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
            NavigationUI.setupWithNavController(navigationView, navController);

            viewModel.getUserMutableLiveData().observe(this, user -> {
                View header = binding.navView.getHeaderView(0);
                TextView navHeaderNameUser = header.findViewById(R.id.navHeaderNameUser);
                TextView navHeaderIdUser = header.findViewById(R.id.navHeaderIdUser);
                navHeaderNameUser.setText(user.getName());
                navHeaderIdUser.setText(String.valueOf(user.getId()));
            });

        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


}