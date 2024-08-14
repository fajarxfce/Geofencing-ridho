package com.example.geofencing.ui.welcome;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
    private int FINE_LOCATION_ACCESS_REQUEST_CODE = 10001;
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

        if (!checkAgreementCache()){
            AgreementDialog.showAgreementDialog(requireContext(), new AgreementDialog.AgreementDialogListener() {
                @Override
                public void onAgreementAccepted() {
                    sf.setPref("agreement_accepted", "true", requireContext());
                    requestLocationPermission();
                }

                @Override
                public void onAgreementRejected() {
                    onDestroy();
                }
            });
        }

        if (mAuth.getCurrentUser() != null && sf.getPref("account_type", requireContext()) != null) {
            navigateBasedOnAccountType();
        }

    }

    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
        } else {
            //Ask for permission
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                //We need to show user a dialog for displaying why the permission is needed and then ask for the permission...
                ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_ACCESS_REQUEST_CODE);
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_ACCESS_REQUEST_CODE);
            }
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

    private boolean checkAgreementCache() {
        return sf.getPref("agreement_accepted", requireContext()) != null;
    }
}