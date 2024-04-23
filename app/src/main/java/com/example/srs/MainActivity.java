package com.example.srs;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    Button get_start_button, go_to_provider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // Get the UID of the current user
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            String currentUserUid = mAuth.getCurrentUser().getUid();

            // Check if the UID exists in the "customers" collection
            FirebaseFirestore.getInstance().collection("customers").document(currentUserUid).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            // User is a customer
                            startActivity(new Intent(getApplicationContext(), CustomerNav.class));
                            finish();
                        } else {
                            // Check if the UID exists in the "providers" collection
                            FirebaseFirestore.getInstance().collection("providers").document(currentUserUid).get()
                                    .addOnSuccessListener(providerDocument -> {
                                        if (providerDocument.exists()) {
                                            // User is a provider
                                            startActivity(new Intent(getApplicationContext(), ProviderNav.class));
                                            finish();
                                        } else {
                                            // User is neither a customer nor a provider
                                            // Handle this case as needed
                                        }
                                    });
                        }
                    });
        } else {
            // User is not logged in, show the MainActivity layout
            setContentView(R.layout.activity_main);
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
            get_start_button = findViewById(R.id.get_started);
            go_to_provider = findViewById(R.id.provider_log_button);

            get_start_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), Login.class);
                    startActivity(intent);
                    finish();
                }
            });

            go_to_provider.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), LoginProvider.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }


}