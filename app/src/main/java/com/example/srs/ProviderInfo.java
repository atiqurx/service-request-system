package com.example.srs;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;

public class ProviderInfo extends AppCompatActivity {
    private TextView nameTextView;
    private TextView emailTextView;
    private TextView addressTextView;
    private TextView priceTextView;
    private TextView ratingTextView;
    private Button chooseProviderButton;
    private String providerUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_info);

        // Get the provider's name from the intent extras
        String providerName = getIntent().getStringExtra("providerName");

        // Get the UID of the logged-in customer
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String customerUid = mAuth.getCurrentUser().getUid();

        // Get reference to the TextViews
        nameTextView = findViewById(R.id.providerNameTextView);
        emailTextView = findViewById(R.id.providerEmailTextView);
        addressTextView = findViewById(R.id.providerAddressTextView);
        priceTextView = findViewById(R.id.providerPriceTextView);
        ratingTextView = findViewById(R.id.providerRatingTextView);
        chooseProviderButton = findViewById(R.id.chooseProviderButton);

        // Get the Firestore instance
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Reference to the "providers" collection
        CollectionReference providersRef = db.collection("providers");

        // Query the document for the specified provider's name
        providersRef.whereEqualTo("name", providerName).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                DocumentSnapshot document = task.getResult().getDocuments().get(0);

                // Get the values from the document
                String name = document.getString("name");
                String email = document.getString("email");
                String address = document.getString("address");
                double price = document.getDouble("price");
                double rating = document.getDouble("rating");

                // Display the provider's information in the respective TextViews
                nameTextView.setText("Name: " + name);
                emailTextView.setText("Email: " + email);
                addressTextView.setText("Address: " + address);
                priceTextView.setText("Price: " + price);
                ratingTextView.setText("Rating: " + rating);

                // Get the UID of the selected provider
                providerUid = document.getId();

            } else {
                nameTextView.setText("Error getting provider information");
                emailTextView.setText("");
                addressTextView.setText("");
                priceTextView.setText("");
                ratingTextView.setText("");
            }
        });

        // Set click listener for the Choose Provider button
        chooseProviderButton.setOnClickListener(v -> {
            // Start the RequestConfirmation activity
            Intent intent = new Intent(ProviderInfo.this, RequestConfirmation.class);

            // Pass the provider's name as an extra to the RequestConfirmation activity
            intent.putExtra("providerName", providerName);

            startActivity(intent);

            // Create a new document under the "requests" collection
            CollectionReference requestsRef = db.collection("requests");
            requestsRef.add(new HashMap<String, Object>() {{
                put("customerUid", customerUid);
                put("providerUid", providerUid);
                put("status", "requested");
            }}).addOnSuccessListener(documentReference -> {
                // Document added successfully
//                System.out.println("Request document added with ID: " + documentReference.getId());
            }).addOnFailureListener(e -> {
                // Handle any errors
//                System.out.println("Error adding request document: " + e.getMessage());
            });
        });
    }
}
