package com.mona.geofencing.ui.parent;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.mona.geofencing.R;
import com.mona.geofencing.databinding.FragmentParentBinding;
import com.mona.geofencing.utils.SharedPreferencesUtil;
import com.mona.geofencing.utils.TokenUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mona.geofencing.viewmodel.ChildViewModel;

public class ParentFragment extends Fragment {


    private static final String TAG = "ParentFragment";
    private FragmentParentBinding binding;
    private SharedPreferencesUtil sf;
    private ChildViewModel childViewModel;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentParentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sf = new SharedPreferencesUtil(requireContext());
        childViewModel = new ViewModelProvider(this).get(ChildViewModel.class);
        mAuth = FirebaseAuth.getInstance();
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_account, R.id.navigation_area, R.id.navigation_childs)
                .build();
//        BottomNavigationView navView = binding.navView;
//        NavHostFragment navHostFragment = (NavHostFragment) getChildFragmentManager().findFragmentById(R.id.nav_host_fragment_parent);
//        NavController navController = navHostFragment.getNavController();
//        NavigationUI.setupWithNavController(navView, navController);
        getAllChilds();
        logRegToken();
        BottomNavigationView navView = binding.navView;
        NavHostFragment navHostFragment = (NavHostFragment) getChildFragmentManager().findFragmentById(R.id.nav_host_fragment_parent);
        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(navView, navController);
        NavigationUI.setupActionBarWithNavController((AppCompatActivity) getActivity(), navController, appBarConfiguration);

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
                        Log.d(TAG, "onComplete: "+token);
                        sf.setPref("parent_fcm_token", token, requireContext());
                    }
                });
    }

    private class GetAccessTokenTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            return TokenUtil.getAccessToken();
        }

        @Override
        protected void onPostExecute(String token) {
            Log.d(TAG, "AccessToken: " + token);
        }
    }

    private void getAllChilds() {
        String uuid = mAuth.getCurrentUser().getUid();
        Log.d(TAG, "getAllChilds: "+uuid);

        childViewModel.getChildrenLiveData().observe(getViewLifecycleOwner(), children -> {
            for (int i = 0; i < children.size(); i++) {
                Log.d(TAG, "getAllChilds: " + children.get(i).getPairCode());
            }
        });
        childViewModel.fetchChildren(uuid);
    }
}