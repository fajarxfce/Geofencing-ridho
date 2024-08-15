package com.example.geofencing.ui.parent.account;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.geofencing.R;
import com.example.geofencing.databinding.FragmentAccountBinding;
import com.example.geofencing.ui.auth.AuthActivity;
import com.example.geofencing.viewmodel.ParentViewModel;
import com.google.firebase.auth.FirebaseAuth;

public class AccountFragment extends Fragment {

    private FragmentAccountBinding binding;
    private ParentViewModel viewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAccountBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String email = auth.getCurrentUser().getEmail();
        viewModel = new ViewModelProvider(this).get(ParentViewModel.class);
        binding.btnLogout.setOnClickListener(v -> viewModel.logout());
        binding.txtUser.setText(email);
        viewModel.getUserLiveData().observe(getViewLifecycleOwner(), firebaseUser -> {
            if (firebaseUser == null) {
                Intent intent = new Intent(getActivity(), AuthActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }
}