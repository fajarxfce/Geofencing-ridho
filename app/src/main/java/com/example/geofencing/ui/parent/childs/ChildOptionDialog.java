package com.example.geofencing.ui.parent.childs;


import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.Navigation;

import com.example.geofencing.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChildOptionDialog extends DialogFragment {

    private static final String TAG = "ChildOptionDialog";

    public ChildOptionDialog() {
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String[] options = {"Lihat Lokasi Anak", "Lihat Riwayat Lokasi", "Area", "Hapus Anak"};

        Bundle bundle = new Bundle();
        bundle.putString("child_uid", getArguments().getString("child_uid"));

        builder.setItems(options, (dialog, which) -> {
            switch (which) {
                case 0:
                    // Lihat Lokasi Anak


                    Navigation.findNavController(getActivity(), R.id.nav_host_fragment_parent).navigate(R.id.action_navigation_childs_to_childLocationFragment, bundle);
                    break;
                case 1:
                    // Lihat Riwayat Lokasi
                    Navigation.findNavController(getActivity(), R.id.nav_host_fragment_parent).navigate(R.id.action_navigation_childs_to_childHistoryLocationFragment, bundle);
                    break;
                case 2:
                    // List Polygon
                    Navigation.findNavController(getActivity(), R.id.nav_host_fragment_parent).navigate(R.id.action_navigation_childs_to_childAreaFragment, bundle);

                    break;
                case 3:
                    // Hapus Anak
//                    new DeleteChildDialog(this.id, this.name, this.pairCode).show(getParentFragmentManager(), "delete_child");

                    break;
            }
        });

        // Create the AlertDialog object and return it.
        return builder.create();
    }
}