package com.example.srs;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

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
        providersRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot document : task.getResult()) {
                    String name = document.getString("name");

                    // Convert both the search query and the stored names to lowercase
                    String lowercaseName = name.toLowerCase();
                    String lowercaseProviderName = providerName.toLowerCase();

                    // Check if the lowercase name contains the lowercase search query
                    if (lowercaseName.contains(lowercaseProviderName)) {
                        // Get the values from the document
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
                        break; // Exit the loop once a match is found
                    }
                }
                // If no match is found
                if (nameTextView.getText().toString().isEmpty()) {
                    nameTextView.setText("No provider found with the specified name");
                    emailTextView.setText("");
                    addressTextView.setText("");
                    priceTextView.setText("");
                    ratingTextView.setText("");
                }
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
            // Check if provider information has been loaded
            if (!nameTextView.getText().toString().isEmpty()) {
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
                }}).addOnSuccessListener(documentReference -> {
                    // Document added successfully
//                    System.out.println("Request document added with ID: " + documentReference.getId());
                }).addOnFailureListener(e -> {
                    // Handle any errors
//                    System.out.println("Error adding request document: " + e.getMessage());
                });
            } else {
                Toast.makeText(ProviderInfo.this, "No provider found with the specified name", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
