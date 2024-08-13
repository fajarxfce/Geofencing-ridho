package com.example.geofencing.ui.auth.register;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.geofencing.R;
import com.example.geofencing.databinding.FragmentParentRegisterBinding;
import com.example.geofencing.viewmodel.ParentRegisterViewModel;

public class ParentRegisterFragment extends Fragment {

    private FragmentParentRegisterBinding binding;
    private ParentRegisterViewModel parentRegisterViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentParentRegisterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        parentRegisterViewModel = new ViewModelProvider(this).get(ParentRegisterViewModel.class);
        binding.register.setOnClickListener(v -> {
            parentRegisterViewModel.register(
                    binding.txtEmail.getText().toString(),
                    binding.txtPassword.getText().toString()
            );
        });

        parentRegisterViewModel.getUserLiveData().observe(getViewLifecycleOwner(), firebaseUser -> {
            if (firebaseUser != null) {
                Toast.makeText(getActivity(), "Registration Successful", Toast.LENGTH_SHORT).show();

            }
        });

        parentRegisterViewModel.getErrorLiveData().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Toast.makeText(getActivity(), "Registration Failed: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}