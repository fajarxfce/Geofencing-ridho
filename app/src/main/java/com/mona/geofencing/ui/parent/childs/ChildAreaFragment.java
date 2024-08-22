package com.mona.geofencing.ui.parent.childs;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mona.geofencing.R;
import com.mona.geofencing.adapter.PolygonAdapter;
import com.mona.geofencing.databinding.FragmentChildAreaBinding;
import com.mona.geofencing.model.Polygon;
import com.mona.geofencing.viewmodel.AreaViewModel;

import java.util.ArrayList;
import java.util.List;

public class ChildAreaFragment extends Fragment {

    private FragmentChildAreaBinding binding;
    private static final String TAG = "ChildAreaFragment";
    private AreaViewModel viewModel;
    private PolygonAdapter polygonAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChildAreaBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String childUid = getArguments().getString("child_uid");
        viewModel = new ViewModelProvider(this).get(AreaViewModel.class);
        setupRecyclerView();

        viewModel.fetchChildPolygons(childUid);
        viewModel.getChildPolygonsLiveData().observe(getViewLifecycleOwner(), this::updateRecyclerView);

        binding.fab.setOnClickListener(v -> {
            Bundle args = new Bundle();
            args.putString("child_uid", getArguments().getString("child_uid"));
            AddPolygonBottomsheet bottomsheet = new AddPolygonBottomsheet();
            bottomsheet.setArguments(args);
            bottomsheet.show(getChildFragmentManager(), "addPolygon");
        });
    }

    private void setupRecyclerView() {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        polygonAdapter = new PolygonAdapter(new ArrayList<>());
        binding.recyclerView.setAdapter(polygonAdapter);
        polygonAdapter.setOnItemLongClickListener(polygon -> {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Hapus Polygon")
                    .setMessage("Apakah anda yakin ingin menghapus polygon?")
                    .setPositiveButton("Ya", (dialog, which) -> {
                        String childUid = getArguments().getString("child_uid");
                        viewModel.deletePolygonFromChild(childUid, polygon.getName());
                    })
                    .setNegativeButton("Tidak", null)
                    .show();
        });
    }

    private void updateRecyclerView(List<Polygon> polygons) {
        polygonAdapter.updateData(polygons);
    }
}