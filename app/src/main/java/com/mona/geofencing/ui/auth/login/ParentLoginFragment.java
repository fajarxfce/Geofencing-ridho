package com.mona.geofencing.ui.auth.login;

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

import com.mona.geofencing.MainActivity;
import com.mona.geofencing.R;
import com.mona.geofencing.databinding.FragmentParentLoginBinding;
import com.mona.geofencing.utils.SharedPreferencesUtil;
import com.mona.geofencing.viewmodel.ParentViewModel;

public class ParentLoginFragment extends Fragment {

    private FragmentParentLoginBinding binding;
    private ParentViewModel viewModel;
    private SharedPreferencesUtil sf;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentParentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(ParentViewModel.class);
        sf = new SharedPreferencesUtil(requireContext());
        binding.loginSignupBtn.setOnClickListener(v -> {
            Navigation.findNavController(getActivity(), R.id.nav_host_fragment_activity_auth).navigate(R.id.action_parentLoginFragment2_to_parentRegisterFragment2);
        });
        binding.loginSubmitBtn.setOnClickListener(v -> {
            if (validateForm()){
                String email = binding.loginEmail.getText().toString().trim();
                String password = binding.loginPassword.getText().toString().trim();
                viewModel.login(email, password);
            }

        });

        viewModel.getUserLiveData().observe(getViewLifecycleOwner(), firebaseUser -> {
            if (firebaseUser != null) {
                Toast.makeText(getActivity(), "Login berhasil", Toast.LENGTH_SHORT).show();
                sf.setPref("account_type", "parent", requireContext());
                Intent intent = new Intent(getActivity(), MainActivity.class);


                startActivity(intent);
                getActivity().finish();
//                Navigation.findNavController(getActivity(), R.id.nav_host_fragment_activity_main).navigate(R.id.action_childLoginFragment2_to_childRegisterFragment2);
            }
        });

        viewModel.getErrorLiveData().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Toast.makeText(getActivity(), "Login Gagal: " + error, Toast.LENGTH_SHORT).show();
            }
        });

        binding.txtForgotPassword.setOnClickListener(v -> {
            ForgotPasswordDialog dialog = new ForgotPasswordDialog();
            dialog.show(getParentFragmentManager(), "ForgotPasswordDialog");
        });
    }

    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(binding.loginEmail.getText().toString())) {
            binding.loginEmail.setError("Silahkan masukkan email valid");
            result = false;
        } else {
            binding.loginEmail.setError(null);
        }

        if (TextUtils.isEmpty(binding.loginPassword.getText().toString())) {
            binding.loginPassword.setError("Silahkan masukkan password valid");
            result = false;
        } else {
            binding.loginPassword.setError(null);
        }

        // Min 6
        if(binding.loginPassword.getText().toString().length() < 6) {
            Toast.makeText(requireContext(), "Password min 6 karakter",
                    Toast.LENGTH_SHORT).show();
        }

        // Must contain @
        if(!binding.loginEmail.getText().toString().contains("@")) {
            Toast.makeText(requireContext(), "Email harus mengandung @",
                    Toast.LENGTH_SHORT).show();
        }

        return result;
    }
}