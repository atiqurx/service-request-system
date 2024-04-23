package com.example.srs;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class RequestStatus extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_status);

        // Get the request ID and provider name from the intent
        String requestId = getIntent().getStringExtra("requestId");
        String providerName = getIntent().getStringExtra("providerName");
        String requestStatus = getIntent().getStringExtra("requestStatus");

        // Find the TextViews in the layout and set the request status and provider name
        TextView statusTextView = findViewById(R.id.text_request_status);
        statusTextView.setText("Request Status: " + requestStatus);

        TextView providerNameTextView = findViewById(R.id.text_provider_name);
        providerNameTextView.setText("Provider: " + providerName);

        // Check if the request status is "completed"
        // Check if the request status is "completed"
        if ("completed".equals(requestStatus)) {
            // If status is "completed", dynamically create a button for rating or reviewing
            Button rateButton = new Button(this);
            rateButton.setText("Rate or Review");
            rateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle button click action (e.g., navigate to rating or review activity)
                    Toast.makeText(RequestStatus.this, "Rate or Review Clicked", Toast.LENGTH_SHORT).show();
                }
            });

            // Find the LinearLayout for buttons and cast it to LinearLayout
            LinearLayout layoutButtons = findViewById(R.id.layout_buttons);

            // Add the button to the layout
            layoutButtons.addView(rateButton);
        }

    }
}
