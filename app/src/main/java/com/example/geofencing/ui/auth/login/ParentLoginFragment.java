package com.example.geofencing.ui.auth.login;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.geofencing.R;
import com.example.geofencing.databinding.FragmentParentLoginBinding;

public class ParentLoginFragment extends Fragment {

    private FragmentParentLoginBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentParentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.loginSignupBtn.setOnClickListener(v -> {
//            Navigation.findNavController(getActivity(), R.id.nav_host_fragment_activity_main).navigate(R.id.action_parentLoginFragment_to_parentFragment);
            Navigation.findNavController(getActivity(), R.id.nav_host_fragment_activity_main).navigate(R.id.action_parentLoginFragment_to_parentRegisterFragment);
        });
    }
}