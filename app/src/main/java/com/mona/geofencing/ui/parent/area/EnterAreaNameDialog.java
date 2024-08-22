package com.mona.geofencing.ui.parent.area;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.mona.geofencing.databinding.DialogEnterAreaNameBinding;
import com.mona.geofencing.repository.AreaRepository;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class EnterAreaNameDialog extends DialogFragment {

    private static final String TAG = "EnterAreaNameDialog";
    DialogEnterAreaNameBinding binding;
    private AreaRepository areaRepository;
    private Context context;

    public EnterAreaNameDialog(Context context) {
        areaRepository = new AreaRepository();
        this.context = context;
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
                Log.d(TAG, "onSuccess: area saved");
                Toast.makeText(context, "Berhasil menyimpan area!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(context, "Gagal menyimpan area!", Toast.LENGTH_SHORT).show();

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