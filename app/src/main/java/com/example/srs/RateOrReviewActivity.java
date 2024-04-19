package com.example.srs;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class RateOrReviewActivity extends AppCompatActivity {

    private RatingBar ratingBar;
    private EditText reviewEditText;
    private Button submitReviewButton;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rate_or_review);

        // Initialize Firebase components
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize UI elements
        ratingBar = findViewById(R.id.ratingBar);
        reviewEditText = findViewById(R.id.reviewEditText);
        submitReviewButton = findViewById(R.id.submitReviewButton);

        // Set up click listener for the submit review button
        submitReviewButton.setOnClickListener(v -> submitReview());
    }

    private void submitReview() {
        // Retrieve rating and review text
        float rating = ratingBar.getRating();
        String review = reviewEditText.getText().toString();

        // Perform any necessary validation
        if (review.isEmpty()) {
            Toast.makeText(this, "Please write a review", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get the current user
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            // User is not signed in, handle appropriately
            Toast.makeText(this, "User not signed in", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save the review data to Firestore
        db.collection("reviews").document(currentUser.getUid())
                .set(new Review(currentUser.getUid(), rating, review))
                .addOnSuccessListener(aVoid -> {
                    // Show a toast message indicating that the review has been submitted
                    Toast.makeText(this, "Review submitted successfully", Toast.LENGTH_SHORT).show();

                    // Finish the activity
                    finish();
                })
                .addOnFailureListener(e -> {
                    // Handle failure to save review
                    Toast.makeText(this, "Failed to submit review", Toast.LENGTH_SHORT).show();
                });
    }

    // Define the Review class inside the RateOrReviewActivity class
    private static class Review {
        private String userId;
        private float rating;
        private String reviewText;

        // Constructor
        public Review(String userId, float rating, String reviewText) {
            this.userId = userId;
            this.rating = rating;
            this.reviewText = reviewText;
        }
    }
}
