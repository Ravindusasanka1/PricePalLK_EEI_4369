package com.example.pricepallk;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class FeedbackActivity extends AppCompatActivity {
    private EditText editTextFeedback;
    private Button buttonSubmit;
    private TextView textViewThankYou;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_feedback);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize UI components
        editTextFeedback = findViewById(R.id.editTextFeedback);
        buttonSubmit = findViewById(R.id.buttonSubmit);
        textViewThankYou = findViewById(R.id.textViewThankYou);

        // Set click listener for submit button
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitFeedback();
            }
        });
    }

    private void submitFeedback() {
        String feedback = editTextFeedback.getText().toString().trim();

        // Validate input
        if (feedback.isEmpty()) {
            editTextFeedback.setError("Please enter your feedback");
            return;
        }

        // Create feedback data map
        Map<String, Object> feedbackData = new HashMap<>();
        feedbackData.put("message", feedback);
        feedbackData.put("timestamp", System.currentTimeMillis());

        // Save to Firestore
        db.collection("feedback")
            .add(feedbackData)
            .addOnSuccessListener(documentReference -> {
                // Show toast message
                Toast.makeText(FeedbackActivity.this, "Feedback sent.", Toast.LENGTH_SHORT).show();

                // Clear the input field
                editTextFeedback.setText("");

                // Show thank you message
                textViewThankYou.setVisibility(View.VISIBLE);
            })
            .addOnFailureListener(e -> {
                Toast.makeText(FeedbackActivity.this,
                        "Failed to send feedback. Please try again.",
                        Toast.LENGTH_SHORT).show();
            });
    }
}