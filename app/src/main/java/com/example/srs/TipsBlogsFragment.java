package com.example.srs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

public class TipsBlogsFragment extends Fragment {
    private FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_tips_blogs, container, false);

        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance();

        // Reference to the TextViews where you want to display the data
        TextView textTitle = root.findViewById(R.id.textTitle);
        TextView textDescription = root.findViewById(R.id.textDescription);

        // Read data from Firestore
        db.collection("tipsBlogs")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            StringBuilder titleDescriptionBuilder = new StringBuilder();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String title = document.getString("title");
                                String description = document.getString("description");

                                // Append title and description to the StringBuilder
                                titleDescriptionBuilder.append(title).append("\n");
                                titleDescriptionBuilder.append(description).append("\n\n");
                            }

                            // Set the concatenated titles and descriptions to the TextViews
                            textTitle.setText(titleDescriptionBuilder.toString());
                            textDescription.setText(""); // Clear description TextView
                        } else {
                            textTitle.setText("Error fetching data");
                            textDescription.setText("");
                        }
                    }
                });




        return root;
    }
}
