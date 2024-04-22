package com.example.srs.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.srs.ForgotPassword;
import com.example.srs.ProviderConfirmation;
import com.example.srs.R;
import com.example.srs.databinding.FragmentDashboardBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private LinearLayout buttonContainer;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        buttonContainer = root.findViewById(R.id.buttonContainer);

        // Get a reference to the "requests" collection
        CollectionReference requestsRef = FirebaseFirestore.getInstance().collection("requests");

        // Get the UID of the current user
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String currentUserUid = mAuth.getCurrentUser().getUid();

        // Query all documents in the "requests" collection
        requestsRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Loop through each document in the result
                for (QueryDocumentSnapshot document : task.getResult()) {
                    // Get the "customerUid" and "providerUid" fields
                    String customerUid = document.getString("customerUid");
                    String providerUid = document.getString("providerUid");

                    // Check if the providerUid matches the current user's UID
                    if (providerUid != null && providerUid.equals(currentUserUid)) {
                        // Get a reference to the "customers" collection
                        CollectionReference customersRef = FirebaseFirestore.getInstance().collection("customers");

                        // Query the document for the specified customer's UID
                        customersRef.document(customerUid).get().addOnCompleteListener(customerTask -> {
                            if (customerTask.isSuccessful()) {
                                // Get the value of the "name" field
                                String customerName = customerTask.getResult().getString("name");

                                Button button = new Button(requireContext());

                                // Check the status field of the document
                                String status = document.getString("status");
                                if (status != null && status.equals("requested")) {
                                    // Create a new button for each document with the customer's name
                                    button.setText(customerName);
                                    buttonContainer.addView(button);
                                }

                                // Set click listener for the button
                                button.setOnClickListener(v -> {
                                    Intent intent = new Intent(requireContext(), ProviderConfirmation.class);
                                    intent.putExtra("requestUid", document.getId());
                                    intent.putExtra("customerName", customerName);
                                    startActivity(intent);
                                });
                            } else {
                                // Handle error getting customer document
                            }
                        });
                    }
                }
            } else {
                // Handle error getting documents
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