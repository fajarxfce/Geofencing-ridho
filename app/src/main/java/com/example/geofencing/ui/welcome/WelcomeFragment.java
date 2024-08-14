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

public class WelcomeFragment extends Fragment {
    private FragmentWelcomeBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWelcomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.btnLoginChild.setOnClickListener(v -> {
            Navigation.findNavController(getActivity(), R.id.nav_host_fragment_activity_main).navigate(R.id.action_welcomeFragment_to_childLoginFragment);
        });
        binding.btnLoginParent.setOnClickListener(v -> {
            Navigation.findNavController(getActivity(), R.id.nav_host_fragment_activity_main).navigate(R.id.action_welcomeFragment_to_parentLoginFragment);
        });
    }
}