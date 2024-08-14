package com.example.geofencing.ui.auth.register;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.geofencing.R;
import com.example.geofencing.databinding.FragmentParentRegisterBinding;
import com.example.geofencing.viewmodel.ParentRegisterViewModel;
import com.google.firebase.auth.FirebaseAuth;

public class ParentRegisterFragment extends Fragment {

    private static final String TAG = "ParentRegisterFragment";
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
            if (!validateForm()) {
                return;
            }

            parentRegisterViewModel.register(
                    binding.txtEmail.getText().toString(),
                    binding.txtPassword.getText().toString()
            );
        });

        parentRegisterViewModel.getUserLiveData().observe(getViewLifecycleOwner(), firebaseUser -> {
            if (firebaseUser != null) {
                Toast.makeText(getActivity(), "Pendaftaran berhasil", Toast.LENGTH_SHORT).show();

            }
        });

        parentRegisterViewModel.getErrorLiveData().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Toast.makeText(getActivity(), "Pendaftaran Gagal: " + error, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(binding.txtEmail.getText().toString())) {
            binding.txtEmail.setError("Silahkan masukkan email valid");
            result = false;
        } else {
            binding.txtEmail.setError(null);
        }

        if (TextUtils.isEmpty(binding.txtPassword.getText().toString())) {
            binding.txtPassword.setError("Silahkan masukkan password valid");
            result = false;
        } else {
            binding.txtPassword.setError(null);
        }

        // Min 6
        if(binding.txtPassword.getText().toString().length() < 6) {
            Toast.makeText(requireContext(), "Password min 6 karakter",
                    Toast.LENGTH_SHORT).show();
        }

        // Must contain @
        if(!binding.txtEmail.getText().toString().contains("@")) {
            Toast.makeText(requireContext(), "Email harus mengandung @",
                    Toast.LENGTH_SHORT).show();
        }

        return result;
    }
}