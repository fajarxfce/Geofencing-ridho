package com.mona.geofencing.ui.parent.childs;


import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.mona.geofencing.R;
import com.mona.geofencing.viewmodel.ChildViewModel;
import com.google.firebase.auth.FirebaseAuth;

public class ChildOptionDialog extends DialogFragment {

    private static final String TAG = "ChildOptionDialog";
    private ChildViewModel viewModel;
    private FirebaseAuth Auth;

    public ChildOptionDialog() {
//        viewModel = new ViewModelProvider(this).get(ChildViewModel.class);

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Auth = FirebaseAuth.getInstance();
        viewModel = new ViewModelProvider(this).get(ChildViewModel.class);
        viewModel.getDeleteLiveData().observe(this, message  -> {
            if (message != null) {
                Log.d(TAG, "onCreateDialog: "+message);
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String[] options = {"Lihat Lokasi Anak", "Lihat Riwayat Lokasi", "Area", "Hapus Anak"};

        Bundle bundle = new Bundle();
        bundle.putString("child_uid", getArguments().getString("child_uid"));
        bundle.putString("pair_code", getArguments().getString("pair_code"));
        builder.setItems(options, (dialog, which) -> {
            switch (which) {
                case 0:
                    // Lihat Lokasi Anak

                    Navigation.findNavController(getActivity(), R.id.nav_host_fragment_activity_main).navigate(R.id.action_navigation_childs_to_childLocationFragment, bundle);
                    break;
                case 1:
                    // Lihat Riwayat Lokasi
                    Navigation.findNavController(getActivity(), R.id.nav_host_fragment_activity_main).navigate(R.id.action_navigation_childs_to_childHistoryLocationFragment, bundle);
                    break;
                case 2:
                    // List Polygon
                    Navigation.findNavController(getActivity(), R.id.nav_host_fragment_activity_main).navigate(R.id.action_navigation_childs_to_childAreaFragment, bundle);

                    break;
                case 3:
                    // Hapus Anak
                    showDeleteConfirmationDialog();
                    break;
            }
        });

        // Create the AlertDialog object and return it.
        return builder.create();
    }

    private void showDeleteConfirmationDialog() {
        new AlertDialog.Builder(getContext())
                .setTitle("Hapus anak")
                .setMessage("Apakah anda yakin ingin menghapus anak ini?")
                .setPositiveButton("Ya", (dialog, which) -> {
                    String parentUid = Auth.getCurrentUser().getUid();
                    String childUid = getArguments().getString("pair_code");
                    viewModel.deleteChildFromParent(parentUid, childUid);
                })
                .setNegativeButton("Tidak", null)
                .show();
    }
}