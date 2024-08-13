package com.example.geofencing.ui.splashscreen;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.geofencing.R;
import com.example.geofencing.databinding.FragmentSplashScreenragmentBinding;
import com.example.geofencing.services.NetworkChangeReceiver;

public class SplashScreenragment extends Fragment implements NetworkChangeReceiver.NetworkChangeListener {

    private FragmentSplashScreenragmentBinding binding;
    private NetworkChangeReceiver network;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSplashScreenragmentBinding.inflate(inflater, container, false);

        network = new NetworkChangeReceiver(this);
        getActivity().registerReceiver(network, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onNetworkChange(boolean isConnected) {
        if (isConnected) {
            new Handler().postDelayed(() -> {
                Navigation.findNavController(requireView()).navigate(R.id.action_splashScreenragment_to_welcomeFragment);
            }, 2000);
        } else {
            Toast.makeText(requireContext(), "Tidak ada koneksi internet", Toast.LENGTH_LONG).show();
        }
    }
}