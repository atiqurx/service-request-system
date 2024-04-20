package com.example.srs;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.graphics.Typeface;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class SearchResults extends AppCompatActivity {
    private LinearLayout buttonLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        // Initialize the LinearLayout where buttons will be added dynamically
        buttonLayout = findViewById(R.id.buttonLayout);

        // Get the Firestore instance
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Reference to the "providers" collection
        CollectionReference providersRef = db.collection("providers");

        // Query all documents in the "providers" collection
        providersRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    // Get the value of the "name" field for each document
                    String name = document.getString("name");

                    // Create a new button
                    Button button = new Button(this);
                    button.setText(name);

                    // Set text color and background color
                    button.setTextColor(getResources().getColor(android.R.color.white));
                    button.setBackgroundResource(R.drawable.round_button);

                    // Set text size and style
                    button.setTextSize(18);
                    button.setTypeface(null, Typeface.BOLD);

                    // Set padding
                    button.setPadding(0, 0, 0, 0);

                    // Set layout parameters
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    layoutParams.setMargins(0, 0, 0, 16); // Add margin between buttons
                    button.setLayoutParams(layoutParams);

                    // Set click listener for the button
                    button.setOnClickListener(v -> startProviderProfileActivity(name));

                    // Add the button to the layout
                    buttonLayout.addView(button);
                }
            } else {
                // Handle the case when data retrieval fails
                // You can display an error message or handle it in any other appropriate way
            }
        });
    }

    // Method to start ProviderInfo activity and pass the provider's name as an extra
    private void startProviderProfileActivity(String providerName) {
        Intent intent = new Intent(this, ProviderInfo.class);
        intent.putExtra("providerName", providerName);
        startActivity(intent);
    }
}
