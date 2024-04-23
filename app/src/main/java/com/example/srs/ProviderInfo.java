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

        providersRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot document : task.getResult()) {
                    String name = document.getString("name");
                    if (name != null) {
                        String lowercaseName = name.toLowerCase();
                        String lowercaseProviderName = providerName.toLowerCase();
                        if (lowercaseName.contains(lowercaseProviderName)) {
                            String email = document.getString("email");
                            String address = document.getString("address");
                            Double price = document.getDouble("price");
                            Double rating = document.getDouble("rating");

                            // Perform null checks before using the values
                            nameTextView.setText("Name: " + name);
                            emailTextView.setText("Email: " + email);
                            addressTextView.setText("Address: " + address);
                            if (price != null && rating != null) {
                                priceTextView.setText("Price: " + price);
                                ratingTextView.setText("Rating: " + rating);
                            } else {
                                // Handle the case when any of the values is null
                                // For example, display a default value or a message
                            }

                            providerUid = document.getId();
                            break;
                        }
                    }
                }
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
                Intent intent = new Intent(ProviderInfo.this, Payment.class);

                // Pass the provider's name as an extra to the Payment activity
                intent.putExtra("providerName", providerName);
                intent.putExtra("providerUid", providerUid);

                startActivity(intent);

            } else {
                Toast.makeText(ProviderInfo.this, "No provider found with the specified name", Toast.LENGTH_SHORT).show();
            }

        });
    }
}
