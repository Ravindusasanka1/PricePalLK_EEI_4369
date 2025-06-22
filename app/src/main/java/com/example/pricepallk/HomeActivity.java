package com.example.pricepallk;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        if (getSupportActionBar() != null) { // Hide the action bar
            getSupportActionBar().hide();
        }

        // connect the Login button
        Button loginButton = findViewById(R.id.button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get email and password from input fields
                EditText emailField = findViewById(R.id.editTextTextEmailAddress4);
                EditText passwordField = findViewById(R.id.editTextNumberPassword);

                String email = emailField.getText().toString().trim();
                String password = passwordField.getText().toString().trim();

                // Validate inputs
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(HomeActivity.this, "Please enter both email and password", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Initialize Firebase if not already initialized
                if (FirebaseApp.getApps(HomeActivity.this).isEmpty()) {
                    FirebaseApp.initializeApp(HomeActivity.this);
                }

                // Get Firestore instance
                FirebaseFirestore db = FirebaseFirestore.getInstance();

                // Create a map with the user data
                Map<String, Object> user = new HashMap<>();
                user.put("email", email);
                user.put("password", password); // Note: In a real app, never store plain passwords
                user.put("timestamp", new Date());

                // Save to Firestore
                db.collection("users")
                    .add(user)
                    .addOnSuccessListener(documentReference -> {
                        Log.d("Firestore", "User added with ID: " + documentReference.getId());
                        Toast.makeText(HomeActivity.this, "Login successful", Toast.LENGTH_SHORT).show();

                        // Navigate to dashboard screen
                        Intent intent = new Intent(HomeActivity.this, DashboardActivity.class);
                        startActivity(intent);
                    })
                    .addOnFailureListener(e -> {
                        Log.w("Firestore", "Error adding user", e);
                        Toast.makeText(HomeActivity.this, "Login failed: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                    });
            }
        });

        // Setup for "Forgot password?" TextView
        TextView forgotPasswordTextView = findViewById(R.id.textView4);
        forgotPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to forgot password screen
                Intent intent = new Intent(HomeActivity.this, ForgotpasswordActivity.class);
                startActivity(intent);
            }
        });

        // Setup for "Don't have an account? Register" TextView
        TextView registerTextView = findViewById(R.id.textView2);
        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to register screen
                Intent intent = new Intent(HomeActivity.this, Registerscreen.class);
                startActivity(intent);
            }
        });
    }
}