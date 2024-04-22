package com.example.srs.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.srs.ProviderInfo;
import com.example.srs.R;
import com.example.srs.SearchResults;
import com.example.srs.TipsBlogsFragment;
import com.example.srs.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private EditText searchEditText;
    private Button searchButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        // Initialize search UI elements
        searchEditText = root.findViewById(R.id.searchEditText);
        searchButton = root.findViewById(R.id.searchButton);

        // Set click listener for the search button
        searchButton.setOnClickListener(v -> {
            String providerName = searchEditText.getText().toString().trim();
            if (!providerName.isEmpty()) {
                // Start ProviderInfo activity with the provider name as an extra
                Intent intent = new Intent(getActivity(), ProviderInfo.class);
                intent.putExtra("providerName", providerName);
                startActivity(intent);
            } else {
                // Show a toast if the search query is empty
                Toast.makeText(getActivity(), "Please enter a provider name", Toast.LENGTH_SHORT).show();
            }
        });

        // Set click listener for the buttons
        Button buttonPlumbing = root.findViewById(R.id.buttonPlumbing);
        Button buttonCleaning = root.findViewById(R.id.buttonCleaning);
        Button buttonElectrical = root.findViewById(R.id.buttonElectrical);
        Button buttonTutoring = root.findViewById(R.id.buttonTutoring);

        buttonPlumbing.setOnClickListener(v -> navigateToSearchResults("Plumbing"));
        buttonCleaning.setOnClickListener(v -> navigateToSearchResults("Cleaning"));
        buttonElectrical.setOnClickListener(v -> navigateToSearchResults("Electrical"));
        buttonTutoring.setOnClickListener(v -> navigateToSearchResults("Tutoring"));

        getChildFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, new TipsBlogsFragment())
                .commit();

        return root;
    }

    // Method to navigate to the search results page
    private void navigateToSearchResults(String serviceSelected) {
        Intent intent = new Intent(getActivity(), SearchResults.class);
        intent.putExtra("serviceSelected", serviceSelected);
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
