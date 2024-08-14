package com.example.geofencing.ui.parent.childs;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.geofencing.R;
import com.example.geofencing.databinding.FragmentChildAreaBinding;

public class ChildAreaFragment extends Fragment {

    FragmentChildAreaBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChildAreaBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.fab.setOnClickListener(v -> {
            Bundle args = new Bundle();
            args.putString("child_uid", getArguments().getString("child_uid"));
            AddPolygonBottomsheet bottomsheet = new AddPolygonBottomsheet();
            bottomsheet.setArguments(args);
            bottomsheet.show(getChildFragmentManager(), "addPolygon");
        });
    }
}