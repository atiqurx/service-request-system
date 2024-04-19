package com.example.srs.ui.profile;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.srs.R;
import com.example.srs.databinding.FragmentProfileBinding;

import com.example.srs.databinding.FragmentProfileBinding;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private DocumentReference docRef;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProfileViewModel profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textProfile;
        profileViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        // Get the Firestore instance
        FirebaseFirestore db = FirebaseFirestore.getInstance();

//        Log.d("ProfileFragment", "Fetching document");
//        docRef.get().addOnSuccessListener(documentSnapshot -> {
//            if (documentSnapshot.exists()) {
//                // Get the data from the document
//                String name = documentSnapshot.getString("name");
//                String phone = documentSnapshot.getString("phone");
//                String address = documentSnapshot.getString("address");
//
//                Log.d("ProfileFragment", "Name: " + name + ", Phone: " + phone + ", Address: " + address);
//
//                // Update the TextViews with the data
//                // ...
//                // Update the TextViews with the data
//                TextView nameTextView = root.findViewById(R.id.profile_name);
//                TextView phoneTextView = root.findViewById(R.id.profile_phone);
//                TextView addressTextView = root.findViewById(R.id.profile_address);
//
//                nameTextView.setText(name);
//                phoneTextView.setText(phone);
//                addressTextView.setText(address);
//            }
//        }).addOnFailureListener(e -> {
//            Log.e("ProfileFragment", "Error getting document", e);
//        });

        // Get the first document from the "customers" collection
        DocumentReference docRef = db.collection("customers").document("002");
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                // Get the data from the document
                String name = documentSnapshot.getString("name");
                String phone = documentSnapshot.getString("phone");
                String address = documentSnapshot.getString("address");

                Log.d("ProfileFragment", "Name: " + name + ", Phone: " + phone + ", Address: " + address);

                // Update the TextViews with the data
                TextView nameTextView = root.findViewById(R.id.profile_name);
                TextView phoneTextView = root.findViewById(R.id.profile_phone);
                TextView addressTextView = root.findViewById(R.id.profile_address);

                nameTextView.setText(name);
                phoneTextView.setText(phone);
                addressTextView.setText(address);
            }
        }).addOnFailureListener(e -> {
            // Handle any errors
            Log.e("ProfileFragment", "Error getting document", e);
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}