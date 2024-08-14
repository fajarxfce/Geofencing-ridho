package com.example.geofencing.ui.parent.childs;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.geofencing.databinding.DialogEnterChildPaircodeBinding;
import com.example.geofencing.viewmodel.ChildViewModel;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class EnterChildPairCodeDialog extends DialogFragment {

    private static final String TAG = "EnterAreaNameDialog";
    DialogEnterChildPaircodeBinding binding;
    private ChildViewModel viewModel;
    public EnterChildPairCodeDialog() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DialogEnterChildPaircodeBinding.inflate(inflater, container, false);
        if (getArguments() != null) {
            List<LatLng> points = getArguments().getParcelableArrayList("points");
        }

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        viewModel = new ViewModelProvider(this).get(ChildViewModel.class);
        binding.btnSubmit.setOnClickListener(view1 -> {
            String pairCode = binding.txtAreaName.getText().toString();
            if (pairCode.isEmpty()) {
                binding.txtAreaName.setError("Kode pairing harus diisi!");
                return;
            }

            if (pairCode.length() != 6) {
                binding.txtAreaName.setError("Kode pairing harus 6 karakter!");
                return;
            }

            viewModel.checkPairCode(pairCode);
        });

        viewModel.getPairCodeExists().observe(getViewLifecycleOwner(), exists -> {
            if (exists) {
                Toast.makeText(getContext(), "Pair code exists", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Kode pairing tidak ditemukan!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        return dialog;
    }

}