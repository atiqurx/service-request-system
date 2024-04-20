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
    private GridLayout buttonGridLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_services, container, false);

        buttonGridLayout = view.findViewById(R.id.buttonGridLayout);

        String[] servicesArray = getResources().getStringArray(R.array.service_provider_array);

        for (String service : servicesArray) {
            addButtonToGridLayout(service);
        }

        return view;
    }

    private void addButtonToGridLayout(String serviceName) {
        Button button = new Button(requireContext());
        button.setLayoutParams(new GridLayout.LayoutParams());
        button.setText(serviceName);
        button.setOnClickListener(v -> {
            // Handle button click here
            Intent intent = new Intent(getActivity(), SearchResults.class);
            startActivity(intent);
        });
        buttonGridLayout.addView(button);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}