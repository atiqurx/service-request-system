package com.example.srs.ui.services;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.GridLayout;
import com.example.srs.R;
import android.widget.Button;
import android.content.Intent;



import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.srs.SearchResults;
import com.example.srs.databinding.FragmentServicesBinding;

public class ServicesFragment extends Fragment {

    private FragmentServicesBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ServicesViewModel servicesViewModel =
                new ViewModelProvider(this).get(ServicesViewModel.class);

        binding = FragmentServicesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textServices;
        servicesViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        // Find all buttons in the GridLayout
        GridLayout buttonGridLayout = root.findViewById(R.id.buttonGridLayout);
        for (int i = 0; i < buttonGridLayout.getChildCount(); i++) {
            View view = buttonGridLayout.getChildAt(i);
            if (view instanceof Button) {
                Button button = (Button) view;
                button.setOnClickListener(v -> {
                    // Handle button click
                    Intent intent = new Intent(getActivity(), SearchResults.class);
                    startActivity(intent);
                });
            }
        }

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}