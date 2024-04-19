package com.example.srs;

import android.os.Bundle;
import android.widget.TextView;

//import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
//
//import androidx.activity.EdgeToEdge;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;

//import com.example.srs.databinding.ActivitySearchResultsBinding;
import com.google.firebase.firestore.CollectionReference;
//import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
//import com.google.firebase.firestore.QuerySnapshot;


public class SearchResults extends AppCompatActivity {
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

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
                    // Get the value of the "name" field for each document
                    String name = (String) document.get("name");
                    result.append(name).append("\n");
                }
                // Display the result in the TextView
                textView.setText(result.toString());
            } else {
                textView.setText("Error getting documents");
            }
        });
    }
}