package com.mona.geofencing.ui.childs;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.mona.geofencing.databinding.DialogChildInfoBinding;
import com.mona.geofencing.model.Child;
import com.mona.geofencing.viewmodel.ChildViewModel;

public class ChildInfoDialog extends DialogFragment {

    private static final String ARG_CHILD = "child_uuid";
    private DialogChildInfoBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogChildInfoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ChildViewModel viewModel = new ViewModelProvider(this).get(ChildViewModel.class);
        String uuid = getArguments().getString(ARG_CHILD);
        viewModel.getChildrenData(uuid);
        viewModel.getChildLiveData().observe(getViewLifecycleOwner(), child -> {
            if (child != null) {
                binding.tvPairCode.setText(child.getPairCode());
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}