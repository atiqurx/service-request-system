package com.example.srs;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Payment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_payment);
        EditText cardNumber = findViewById(R.id.card_number);
        EditText expiryDate = findViewById(R.id.card_expiry);
        EditText cvv = findViewById(R.id.card_cvv);
        Button submit = findViewById(R.id.buttonSubmit);

        submit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (isValidCardInfo(cardNumber.getText().toString(), expiryDate.getText().toString(), cvv.getText().toString())) {
                    startActivity(new Intent(Payment.this, RequestConfirmation.class));
                } else {
                    Toast.makeText(Payment.this, "Invalid card details", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean isValidCardInfo(String cardNumber, String expiryDate, String cvv) {
        // Simple validation logic (You should improve it according to your needs)
        return cardNumber.length() == 16 && expiryDate.matches("\\d{2}/\\d{2}") && cvv.length() == 3;
    }
}