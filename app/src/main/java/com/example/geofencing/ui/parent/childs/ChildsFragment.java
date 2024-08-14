package com.example.geofencing.ui.parent.childs;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.geofencing.R;
import com.example.geofencing.adapter.ChildAdapter;
import com.example.geofencing.databinding.FragmentChildsBinding;
import com.example.geofencing.viewmodel.ChildViewModel;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class ChildsFragment extends Fragment {

    private static final String TAG = "HomeFragment";
    private FragmentChildsBinding binding;
    private ChildViewModel viewModel;
    private ChildAdapter adapter;
    private FirebaseAuth Auth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChildsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(ChildViewModel.class);
        setupRecyclerView();

        binding.fabAddChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 EnterChildPairCodeDialog dialog = new EnterChildPairCodeDialog();
                 dialog.show(getParentFragmentManager(), dialog.getTag());
            }
        });

        viewModel.getChildrenLiveData().observe(getViewLifecycleOwner(), children -> {
            for (int i = 0; i < children.size(); i++) {
                Log.d(TAG, "onViewCreated: "+children.get(i).getPairCode());
            }
            adapter.updateChildList(children);
            adapter.setOnItemClickListener(new ChildAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int i) {
                    String childUid = children.get(i).getChildId();
                    String pairCode = children.get(i).getPairCode();
                    Bundle args = new Bundle();
                    args.putString("child_uid", childUid);
                    args.putString("pair_code", pairCode);

                    ChildOptionDialog dialog = new ChildOptionDialog();
                    dialog.setArguments(args);
                    dialog.show(getParentFragmentManager(), dialog.getTag());
                }
            });
        });

        String parentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        viewModel.fetchChildren(parentUid);

    }

    private void setupRecyclerView() {
        adapter = new ChildAdapter(new ArrayList<>());
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        binding.recyclerView.setAdapter(adapter);

    }


}