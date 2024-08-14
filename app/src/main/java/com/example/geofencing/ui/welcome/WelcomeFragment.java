package com.example.geofencing.ui.welcome;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.geofencing.R;
import com.example.geofencing.databinding.FragmentWelcomeBinding;
import com.example.geofencing.utils.SharedPreferencesUtil;
import com.google.firebase.auth.FirebaseAuth;

public class WelcomeFragment extends Fragment {
    private FragmentWelcomeBinding binding;
    private FirebaseAuth mAuth;
    private SharedPreferencesUtil sf;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWelcomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        sf = new SharedPreferencesUtil(requireContext());
        binding.btnLoginChild.setOnClickListener(v -> {
            Navigation.findNavController(getActivity(), R.id.nav_host_fragment_activity_main).navigate(R.id.action_welcomeFragment_to_childLoginFragment);
        });
        binding.btnLoginParent.setOnClickListener(v -> {
            Navigation.findNavController(getActivity(), R.id.nav_host_fragment_activity_main).navigate(R.id.action_welcomeFragment_to_parentLoginFragment);
        });

        if (mAuth.getCurrentUser() != null && sf.getPref("account_type", requireContext()) != null) {
            navigateBasedOnAccountType();
        }

    }

    private void navigateBasedOnAccountType() {
        String accountType = sf.getPref("account_type", requireContext());
        if ("child".equals(accountType)) {
            Navigation.findNavController(getActivity(), R.id.nav_host_fragment_activity_main).navigate(R.id.action_welcomeFragment_to_childFragment);
        } else {
            Navigation.findNavController(getActivity(), R.id.nav_host_fragment_activity_main).navigate(R.id.action_welcomeFragment_to_parentFragment);
        }
    }
}