package com.example.srs;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class RequestConfirmation extends AppCompatActivity {

    private static final String TAG = "RequestConfirmation";
    private TextView providerNameTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_confirmation);

        // Get the provider's name from the intent extras
        String providerName = getIntent().getStringExtra("providerName");

        // Get reference to the TextView
        providerNameTextView = findViewById(R.id.providerNameTextView);

        if (providerName != null) {
            // Display the message
            providerNameTextView.setText("Request sent to " + providerName);
        } else {
            Log.d(TAG, "Provider name is null");
        }
    }
}