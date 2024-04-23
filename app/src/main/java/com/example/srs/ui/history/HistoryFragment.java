package com.example.srs.ui.history;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.srs.RequestStatus; // Assuming you have a RequestStatusActivity
import com.example.srs.databinding.FragmentHistoryBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public class HistoryFragment extends Fragment {

    private FragmentHistoryBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHistoryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Get a reference to the "buttonContainer" LinearLayout using view binding
        LinearLayout buttonContainer = binding.buttonContainer;

        // Get the UID of the current user
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String currentUserUid = mAuth.getCurrentUser().getUid();

        // Get a reference to the "requests" collection
        CollectionReference requestsRef = FirebaseFirestore.getInstance().collection("requests");

        // Query documents where the customerUid matches the UID of the current user
        requestsRef.whereEqualTo("customerUid", currentUserUid).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Loop through each document in the result
                for (QueryDocumentSnapshot document : task.getResult()) {
                    // Get the "providerUid" and "customerUid" fields
                    String providerUid = document.getString("providerUid");

                    // Get the provider name using the provider UID
                    FirebaseFirestore.getInstance().collection("providers")
                            .document(providerUid)
                            .get()
                            .addOnSuccessListener(providerDocument -> {
                                String providerName = providerDocument.getString("name");
                                addButtonToLayout(providerName, buttonContainer, document.getId()); // Pass request ID to addButtonToLayout
                            });
                }
            } else {
                // Handle error
            }
        });

        return root;
    }

    private void addButtonToLayout(String providerName, LinearLayout buttonContainer, String requestId) {
        Button button = new Button(requireContext());
        button.setText(providerName);
        button.setOnClickListener(view -> {
            // Navigate to RequestStatusActivity when the button is clicked
            Intent intent = new Intent(requireContext(), RequestStatus.class);
            intent.putExtra("requestId", requestId);
            intent.putExtra("providerName", providerName);// Pass request ID to RequestStatusActivity
            startActivity(intent);
        });
        buttonContainer.addView(button);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
