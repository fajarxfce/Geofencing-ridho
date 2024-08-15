
package com.example.geofencing.ui.parent.area;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.geofencing.R;
import com.example.geofencing.adapter.PolygonAdapter;
import com.example.geofencing.databinding.FragmentAreaBinding;
import com.example.geofencing.model.CustomLatLng;
import com.example.geofencing.model.Polygon;
import com.example.geofencing.viewmodel.AreaViewModel;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class AreaFragment extends Fragment {

    private static final String TAG = "AreaFragment";
    private FragmentAreaBinding binding;
    private AreaViewModel viewModel;
    private FirebaseAuth mAuth;
    private PolygonAdapter polygonAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAreaBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        viewModel = new ViewModelProvider(this).get(AreaViewModel.class);
        setupRecyclerView();
        binding.fabAddArea.setOnClickListener(view1 -> {
             Navigation.findNavController(view1).navigate(R.id.action_navigation_area_to_addAreaFragment);
        });

        String userId = mAuth.getCurrentUser().getUid();
        viewModel.fetchAreasForParent(userId);
        viewModel.getAreasLiveData().observe(getViewLifecycleOwner(), areas -> {
            if (areas != null) {
                updateRecyclerView(areas);
                polygonAdapter.setOnItemClickListener(new PolygonAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position, Polygon polygon) {
                        Bundle args = new Bundle();
                        ArrayList<Parcelable> points = new ArrayList<>(polygon.getPoints());
                        args.putParcelableArrayList("points", points);
                        Navigation.findNavController(getActivity(), R.id.nav_host_fragment_activity_main)
                                .navigate(R.id.action_navigation_area_to_detailAreaFragment, args);
                    }
                });
            }
        });

    }

    private void setupRecyclerView() {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        polygonAdapter = new PolygonAdapter(new ArrayList<>());
        binding.recyclerView.setAdapter(polygonAdapter);
    }

    private void updateRecyclerView(List<Polygon> polygons) {
        polygonAdapter.updateData(polygons);
    }
}