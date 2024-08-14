package com.example.geofencing.ui.parent.area;

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

import com.example.geofencing.databinding.DialogEnterAreaNameBinding;
import com.example.geofencing.repository.AreaRepository;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class EnterAreaNameDialog extends DialogFragment {

    private static final String TAG = "EnterAreaNameDialog";
    DialogEnterAreaNameBinding binding;
    private AreaRepository areaRepository;

    public EnterAreaNameDialog() {
        areaRepository = new AreaRepository();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DialogEnterAreaNameBinding.inflate(inflater, container, false);
        if (getArguments() != null) {
            List<LatLng> points = getArguments().getParcelableArrayList("points");
            binding.btnSubmit.setOnClickListener(v -> {
                saveArea(points);
            });
        }

        return binding.getRoot();
    }

    private void saveArea(List<LatLng> points) {
        String polygonName = binding.txtAreaName.getText().toString().trim();
        if (polygonName.isEmpty()) {
            binding.txtAreaName.setError("Polygon name is required");
            return;
        }

        areaRepository.saveArea(polygonName, points, new AreaRepository.SaveAreaCallback() {
            @Override
            public void onSuccess() {
                if (isAdded()) {
                    Toast.makeText(requireContext(), "Berhasil menyimpan area!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                if (isAdded()) {
                    Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show();
                }
            }
        });
        dismiss();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        return dialog;
    }

}