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

        // Get the provider's name from the intent extras
        String providerName = getIntent().getStringExtra("providerName");

        // Get reference to the TextView
        textView = findViewById(R.id.textView);

        // Get the Firestore instance
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Reference to the "providers" collection
        CollectionReference providersRef = db.collection("providers");

        // Query the document for the specified provider's name
        providersRef.whereEqualTo("name", providerName).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                QueryDocumentSnapshot document = (QueryDocumentSnapshot) task.getResult().getDocuments().get(0);// Assuming there's only one document for each provider
                // Get the value of the "name", "email", and "address" fields
                String name = (String) document.get("name");
                String email = (String) document.get("email");
                String address = (String) document.get("address");

                // Display the provider's information
                StringBuilder result = new StringBuilder();
                result.append("Name: ").append(name).append("\n");
                result.append("Email: ").append(email).append("\n");
                result.append("Address: ").append(address).append("\n\n");
                textView.setText(result.toString());
            } else {
                textView.setText("Error getting provider information");
            }
        });
    }

}
