package com.example.geofencing;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.geofencing.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements NavController.OnDestinationChangedListener {

    private ActivityMainBinding binding;
    NavController navController;
    private AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        setSupportActionBar(binding.toolbar);
//        if (getSupportActionBar() != null) {
//
//        }
//        navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
//        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
//        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_main);
//        navController = navHostFragment.getNavController();
//        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
//        navController.addOnDestinationChangedListener(this);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_account, R.id.navigation_area,R.id.navigation_childs)
                .build();

        navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        navController.addOnDestinationChangedListener(this);

    }

    @Override
    public boolean onSupportNavigateUp() {
        navController.navigateUp();
        return super.onSupportNavigateUp();
    }


    @Override
    public void onDestinationChanged(@NonNull NavController navController, @NonNull NavDestination navDestination, @Nullable Bundle bundle) {
//        if (navDestination.getId() == R.id.splashScreenragment2
//        || navDestination.getId() == R.id.welcomeFragment2) {
//            getSupportActionBar().hide();
//        } else {
//            getSupportActionBar().show();
//        }

        int destination = navDestination.getId();
        if (destination == R.id.addAreaFragment
        || destination == R.id.childHistoryLocationFragment
        || destination == R.id.detailAreaFragment
        || destination == R.id.childLocationFragment
        || destination == R.id.childAreaFragment){
            binding.navView.setVisibility(View.GONE);
        } else {
            binding.navView.setVisibility(View.VISIBLE);
        }
    }
}