package com.example.srs;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;


import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Arrays;
import java.util.List;

public class SearchResults extends AppCompatActivity {

    private LinearLayout mainLayout;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        String serviceSelected = getIntent().getStringExtra("serviceSelected");


        // Get reference to the LinearLayout
        mainLayout = findViewById(R.id.main);

        // Get the Firestore instance
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Reference to the "providers" collection
        CollectionReference providersRef = db.collection("providers");

        // Query all documents in the "providers" collection
        providersRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                boolean providersFound = false;
                for (QueryDocumentSnapshot document : task.getResult()) {
                    // Get the value of the "servicesOffered" field for each document
                    String servicesOfferedString = document.getString("servicesOffered");
                    if (servicesOfferedString != null) {
                        String[] servicesOfferedArray = servicesOfferedString.split(",");
                        ArrayList<String> servicesOffered = new ArrayList<>(Arrays.asList(servicesOfferedArray));
                        if (servicesOffered.contains(serviceSelected)) {
                            // Create and add a button for this document
                            // (remaining code for button creation and addition)
                            Log.d("SearchResults", "Service offered: " + servicesOffered);
                            // Get the value of the "name" field for each document
                            String name = document.getString("name");

                            // Create a new button for each document
                            Button button = new Button(SearchResults.this);
                            button.setLayoutParams(new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT));
                            button.setText(name);
                            button.setOnClickListener(v -> {
                                // Start the Provider_Profile activity, passing the provider's name as an extra
                                startProviderProfileActivity(name);
                            });

                            // Add the button to the LinearLayout
                            mainLayout.addView(button);

                            providersFound = true;
                        }
                    }
                }
                // If no providers are found, display a message
                if (!providersFound) {
                    TextView textView = new TextView(SearchResults.this);
                    textView.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
                    textView.setText("No providers with the selected service at the moment");
                    mainLayout.addView(textView);
                }
            } else {
                // Handle the case when data retrieval fails
                // You can display an error message or handle it in any other appropriate way
            }


        });
    }

    // Method to start Provider_Info activity and pass the provider's name as an extra
    private void startProviderProfileActivity(String providerName) {
        Intent intent = new Intent(this, ProviderInfo.class);
        intent.putExtra("providerName", providerName);
        startActivity(intent);
    }

}

