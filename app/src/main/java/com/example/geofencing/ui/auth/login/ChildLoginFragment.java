package com.example.geofencing.ui.auth.login;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.geofencing.MainActivity;
import com.example.geofencing.R;
import com.example.geofencing.databinding.FragmentChildLoginBinding;
import com.example.geofencing.ui.childs.ChildFragment;
import com.example.geofencing.utils.SharedPreferencesUtil;
import com.example.geofencing.viewmodel.ChildViewModel;

public class ChildLoginFragment extends Fragment {

    private FragmentChildLoginBinding binding;
    private ChildViewModel viewModel;
    private SharedPreferencesUtil sf;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChildLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(ChildViewModel.class);
        sf = new SharedPreferencesUtil(requireContext());
        binding.register.setOnClickListener(v -> {

            Navigation.findNavController(getActivity(), R.id.nav_host_fragment_activity_auth).navigate(R.id.action_childLoginFragment2_to_childRegisterFragment2);
        });

        binding.login.setOnClickListener(v -> {
            String email = binding.txtEmail.getText().toString().trim();
            String password = binding.txtPassword.getText().toString().trim();
            if (!validateForm()) {
                return;
            }

            viewModel.login(email, password);

        });

        viewModel.getUserLiveData().observe(getViewLifecycleOwner(), firebaseUser -> {
            if (firebaseUser != null) {
                Toast.makeText(getActivity(), "Login berhasil", Toast.LENGTH_SHORT).show();
                sf.setPref("account_type", "child", requireContext());
                Intent intent = new Intent(getActivity(), ChildFragment.class);
                startActivity(intent);
                getActivity().finish();
//                Navigation.findNavController(getActivity(), R.id.nav_host_fragment_activity_main).navigate(R.id.action_childLoginFragment_to_childFragment);
            }
        });

        viewModel.getErrorLiveData().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Toast.makeText(getActivity(), "Login Gagal: " + error, Toast.LENGTH_SHORT).show();
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