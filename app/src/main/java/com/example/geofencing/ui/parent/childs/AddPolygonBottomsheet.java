package com.example.geofencing.ui.parent.childs;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.geofencing.adapter.PolygonAdapter;
import com.example.geofencing.databinding.AddChildPolygonBottomsheetBinding;
import com.example.geofencing.model.Polygon;
import com.example.geofencing.viewmodel.AreaViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

public class AddPolygonBottomsheet extends BottomSheetDialogFragment {

    private static final String TAG = "AddPolygonBottomsheet";
    private AddChildPolygonBottomsheetBinding binding;
    private AreaViewModel viewModel;
    private FirebaseAuth mAuth;
    private PolygonAdapter polygonAdapter;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = AddChildPolygonBottomsheetBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String parentUid = mAuth.getUid();
        String childUid = getArguments().getString("child_uid");
        viewModel = new ViewModelProvider(this).get(AreaViewModel.class);
        setupRecyclerView();
        viewModel.fetchUnassignedPolygons(parentUid, childUid);
        viewModel.getUnassignedPolygonsLiveData().observe(getViewLifecycleOwner(), this::updateRecyclerView);
    }

    private void setupRecyclerView() {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        polygonAdapter = new PolygonAdapter(new ArrayList<>());
        binding.recyclerView.setAdapter(polygonAdapter);
    }

    private void updateRecyclerView(List<Polygon> polygons) {
        polygonAdapter.updateData(polygons);
        polygonAdapter.setOnItemClickListener((i, polygon) -> {
            String childUid = getArguments().getString("child_uid");
            viewModel.assignPolygonToChild(childUid, polygons.get(i).getName(), polygons.get(i).getPoints());
        });
    }
}
