package com.mona.geofencing.ui.auth.register;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mona.geofencing.R;
import com.mona.geofencing.databinding.FragmentChildRegisterBinding;
import com.mona.geofencing.viewmodel.ChildViewModel;

public class ChildRegisterFragment extends Fragment {

    private FragmentChildRegisterBinding binding;
    private ChildViewModel viewModel;

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
        viewModel = new ViewModelProvider(this).get(ChildViewModel.class);
        binding.register.setOnClickListener(v -> {
            if (!validateForm() && !validatePasswords()) {
                Toast.makeText(getActivity(), "Silahkan isi form dengan benar!", Toast.LENGTH_SHORT).show();
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
                Navigation.findNavController(view).navigate(R.id.action_childRegisterFragment2_to_childLoginFragment2);
            }
        });

        viewModel.getErrorLiveData().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                if (error.contains("email address is already in use")) {
                    error = "Email sudah digunakan";
                } else if (error.contains("The email address is badly formatted.")) {
                    error = "Format email salah";
                }
                Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getRegisterLiveData().observe(getViewLifecycleOwner(), register -> {
            if (register != null) {
                Toast.makeText(getActivity(), register, Toast.LENGTH_SHORT).show();
            }
        });

        binding.txtPassword.addTextChangedListener(passwordTextWatcher);
        binding.txtPasswordConfirm.addTextChangedListener(passwordTextWatcher);
    }

    private final TextWatcher passwordTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // No action needed
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            validatePasswords();
        }

        @Override
        public void afterTextChanged(Editable s) {
            // No action needed
        }
    };

    private boolean validatePasswords() {
        boolean result = true;
        String password = binding.txtPassword.getText().toString();
        String confirmPassword = binding.txtPasswordConfirm.getText().toString();

        if (!password.equals(confirmPassword)) {
            binding.txtPasswordConfirm.setError("Passwords do not match");
            result = false;
        } else {
            binding.txtPasswordConfirm.setError(null);
            result = true;
        }
        return result;
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
        if (binding.txtPassword.getText().toString().length() < 6) {
            Toast.makeText(requireContext(), "Password min 6 karakter",
                    Toast.LENGTH_SHORT).show();
        }

        // Must contain @
        if (!binding.txtEmail.getText().toString().contains("@")) {
            Toast.makeText(requireContext(), "Email harus mengandung @",
                    Toast.LENGTH_SHORT).show();
        }

        return result;
    }
}