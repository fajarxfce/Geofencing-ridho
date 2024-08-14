package com.example.geofencing.ui.auth.register;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.geofencing.R;
import com.example.geofencing.databinding.FragmentChildRegisterBinding;
import com.example.geofencing.viewmodel.ChildRegisterViewModel;
import com.example.geofencing.viewmodel.ParentRegisterViewModel;

public class ChildRegisterFragment extends Fragment {

    private FragmentChildRegisterBinding binding;
    private ChildRegisterViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentChildRegisterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(ChildRegisterViewModel.class);
        binding.register.setOnClickListener(v -> {
            if (!validateForm()) {
                return;
            }

            viewModel.register(
                    binding.txtEmail.getText().toString(),
                    binding.txtPassword.getText().toString()
            );
        });

        viewModel.getUserLiveData().observe(getViewLifecycleOwner(), firebaseUser -> {
            if (firebaseUser != null) {
                Toast.makeText(getActivity(), "Pendaftaran berhasil", Toast.LENGTH_SHORT).show();

            }
        });

        viewModel.getErrorLiveData().observe(getViewLifecycleOwner(), error -> {
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