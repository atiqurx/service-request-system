package com.example.srs;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class ProviderInfo extends AppCompatActivity {
    private TextView nameTextView;
    private TextView emailTextView;
    private TextView addressTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_info);

        // Get the provider's name from the intent extras
        String providerName = getIntent().getStringExtra("providerName");

        // Get reference to the TextViews
        nameTextView = findViewById(R.id.providerNameTextView);
        emailTextView = findViewById(R.id.providerEmailTextView);
        addressTextView = findViewById(R.id.providerAddressTextView);

        // Get the Firestore instance
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Reference to the "providers" collection
        CollectionReference providersRef = db.collection("providers");

        // Query the document for the specified provider's name
        providersRef.whereEqualTo("name", providerName).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                QueryDocumentSnapshot document = (QueryDocumentSnapshot) task.getResult().getDocuments().get(0); // Assuming there's only one document for each provider
                // Get the value of the "name", "email", and "address" fields
                String name = document.getString("name");
                String email = document.getString("email");
                String address = document.getString("address");

                // Display the provider's information in the respective TextViews
                nameTextView.setText("Name: " + name);
                emailTextView.setText("Email: " + email);
                addressTextView.setText("Address: " + address);
            } else {
                nameTextView.setText("Error getting provider information");
                emailTextView.setText("");
                addressTextView.setText("");
            }
        });
    }

}