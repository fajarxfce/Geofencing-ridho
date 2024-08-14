package com.example.geofencing.ui.parent.childs;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.geofencing.R;
import com.example.geofencing.databinding.FragmentChildAreaBinding;
import com.example.geofencing.viewmodel.AreaViewModel;

public class ChildAreaFragment extends Fragment {

    private FragmentChildAreaBinding binding;
    private static final String TAG = "ChildAreaFragment";
    private AreaViewModel viewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChildAreaBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(AreaViewModel.class);

        binding.fab.setOnClickListener(v -> {
            Bundle args = new Bundle();
            args.putString("child_uid", getArguments().getString("child_uid"));
            AddPolygonBottomsheet bottomsheet = new AddPolygonBottomsheet();
            bottomsheet.setArguments(args);
            bottomsheet.show(getChildFragmentManager(), "addPolygon");
        });
    }
}