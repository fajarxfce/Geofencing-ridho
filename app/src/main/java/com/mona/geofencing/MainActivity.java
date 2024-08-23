package com.mona.geofencing;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mona.geofencing.databinding.ActivityMainBinding;
import com.mona.geofencing.model.Child;
import com.mona.geofencing.utils.SharedPreferencesUtil;
import com.mona.geofencing.utils.TokenUtil;
import com.mona.geofencing.viewmodel.ChildViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity implements NavController.OnDestinationChangedListener {

    private ActivityMainBinding binding;
    NavController navController;
    private AppBarConfiguration appBarConfiguration;
    private ChildViewModel childViewModel;
    private FirebaseAuth mAuth;
    private SharedPreferencesUtil sf;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sf = new SharedPreferencesUtil(this);
        childViewModel = new ViewModelProvider(this).get(ChildViewModel.class);
        mAuth = FirebaseAuth.getInstance();
        logRegToken();
        getAllChilds();

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

    private void getAllChilds() {
        String uuid = mAuth.getCurrentUser().getUid();
        childViewModel.fetchChildren(uuid);
        childViewModel.getChildrenLiveData().observe(this, this::updateFcmToken);

    }

    private void updateFcmToken(List<Child> childId) {
        String parentUid = mAuth.getCurrentUser().getUid();
        String fcmToken = sf.getPref("parent_fcm_token", this);
        childViewModel.updateFcmToken(parentUid, childId, fcmToken);
    }

    private void logRegToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        String token = task.getResult();
                        sf.setPref("parent_fcm_token", token, MainActivity.this);
                    }
                });
    }
}