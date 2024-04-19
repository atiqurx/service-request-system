package com.example.srs;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class SearchResults extends AppCompatActivity {
    private Button button1;
    private Button button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        // Get references to the buttons
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);

        // Set click listeners for the buttons
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the Provider_Profile activity, passing the provider's name as an extra
                startProviderProfileActivity(button1.getText().toString());
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the Provider_Profile activity, passing the provider's name as an extra
                startProviderProfileActivity(button2.getText().toString());
            }
        });

        // Get the Firestore instance
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Reference to the "providers" collection
        CollectionReference providersRef = db.collection("providers");

        // Query all documents in the "providers" collection
        providersRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                int i = 1;
                for (QueryDocumentSnapshot document : task.getResult()) {
                    // Get the value of the "name" field for each document
                    String name = document.getString("name");

                    // Set the text of button1 for the first document, and button2 for the second document
                    if (i == 1) {
                        button1.setText(name);
                        i++;
                    } else if (i == 2) {
                        button2.setText(name);
                        break; // Exit the loop after setting the text for button2
                    }
                }
            } else {
                // Handle the case when data retrieval fails
                // You can display an error message or handle it in any other appropriate way
            }
        });
    }

    // Method to start Provider_Profile activity and pass the provider's name as an extra
    private void startProviderProfileActivity(String providerName) {
        Intent intent = new Intent(this, Provider_Profile.class);
        intent.putExtra("providerName", providerName);
        startActivity(intent);
    }

}
