package com.example.srs.ui.providerHome;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.srs.R;
import com.example.srs.RequestCompletion;
import com.example.srs.databinding.FragmentProviderHomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ProviderHomeFragment extends Fragment {

    private FragmentProviderHomeBinding binding;
    private LinearLayout dataContainer;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProviderHomeViewModel providerhomeViewModel =
                new ViewModelProvider(this).get(ProviderHomeViewModel.class);

        binding = FragmentProviderHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textProviderHome;
        providerhomeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        dataContainer = root.findViewById(R.id.acceptedRequestsContainer);

        // Get a reference to the "requests" collection
        CollectionReference requestsRef = FirebaseFirestore.getInstance().collection("requests");

        // Get a reference to the "customers" collection
        CollectionReference customersRef = FirebaseFirestore.getInstance().collection("customers");

        // Get the UID of the current user
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String currentUserUid = mAuth.getCurrentUser().getUid();

        requestsRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Loop through each document in the result
                // Loop through each document in the result
                for (QueryDocumentSnapshot document : task.getResult()) {
                    // Get the "customerUid", "providerUid", and "status" fields
                    String customerUid = document.getString("customerUid");
                    String providerUid = document.getString("providerUid");
                    String status = document.getString("status");

                    // Check if the providerUid matches the current user's UID and status is "accepted"
                    if (providerUid != null && providerUid.equals(currentUserUid) && "accepted".equals(status)) {
                        // Query the "customers" collection for the customer's UID
                        customersRef.document(customerUid).get().addOnCompleteListener(customerTask -> {
                            if (customerTask.isSuccessful()) {
                                // Get the customer's name
                                String customerName = customerTask.getResult().getString("name");

                                // Create a new button for each document with the customer's name
                                Button button = new Button(requireContext());
                                button.setText("Customer Name: " + customerName);
                                dataContainer.addView(button);

                                // Set click listener for the button
                                button.setOnClickListener(v -> {
                                    // Start RequestCompletion activity
                                    Intent intent = new Intent(requireContext(), RequestCompletion.class);
                                    intent.putExtra("customerName", customerName);
                                    intent.putExtra("status", status);
                                    intent.putExtra("requestUid", document.getId());
                                    startActivity(intent);
                                });
                            } else {
                                Log.d("FirestoreData", "Error getting customer document: ", customerTask.getException());
                            }
                        });
                    }
                }
            } else {
                Log.d("FirestoreData", "Error getting documents: ", task.getException());
            }
        });


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
