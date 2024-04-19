package com.example.srs;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class Provider_Profile extends AppCompatActivity {
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_profile);

        // Get reference to the TextView
        textView = findViewById(R.id.textView);

        // Get the Firestore instance
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Reference to the "provider" collection
        CollectionReference providersRef = db.collection("providers");

        // Query all documents in the "provider" collection
        providersRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                StringBuilder result = new StringBuilder();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    // Get the value of the "name", "email", and "address" fields for each document
                    String name = (String) document.get("name");
                    String email = (String) document.get("email");
                    String address = (String) document.get("address");

                    // Append the name, email, and address to the result
                    result.append("Name: ").append(name).append("\n");
                    result.append("Email: ").append(email).append("\n");
                    result.append("Address: ").append(address).append("\n\n");
                }
                // Display the result in the TextView
                textView.setText(result.toString());
            } else {
                textView.setText("Error getting documents");
            }
        });
    }
}
