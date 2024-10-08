package com.mona.geofencing.ui.parent.childs;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mona.geofencing.R;
import com.mona.geofencing.adapter.LocationHistoryAdapter;
import com.mona.geofencing.databinding.FragmentChildHistoryLocationBinding;
import com.mona.geofencing.viewmodel.ChildViewModel;

import java.util.ArrayList;

public class ChildHistoryLocationFragment extends Fragment {

    private static final String TAG = "ChildHistoryLocationFragment";
    private FragmentChildHistoryLocationBinding binding;
    private ChildViewModel viewModel;
    private LocationHistoryAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChildHistoryLocationBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(ChildViewModel.class);
        String childUid = getArguments().getString("child_uid");
        setupRecyclerView();
        viewModel.fetchLocationHistory(childUid);
        viewModel.getLocationHistoryLiveData().observe(getViewLifecycleOwner(), locationHistories -> {
            adapter.updateLocationHistoryList(locationHistories);
        });
    }

    private void setupRecyclerView() {
        adapter = new LocationHistoryAdapter(new ArrayList<>());
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);
    }
}