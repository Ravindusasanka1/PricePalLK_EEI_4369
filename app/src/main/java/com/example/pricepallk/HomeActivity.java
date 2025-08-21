package com.example.pricepallk;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomeActivity extends AppCompatActivity {
    // Add FirebaseAuth at class level
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        if (getSupportActionBar() != null) { // Hide the action bar
            getSupportActionBar().hide();
        }

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

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

                // Password strength validation
                if (!isValidPassword(password)) {
                    return;
                }

                // Sign in with Firebase Authentication
                mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(HomeActivity.this, task -> {
                        if (task.isSuccessful()) {
                            // Sign in success
                            Log.d("Firebase", "signInWithEmail:success");
                            Toast.makeText(HomeActivity.this, "Login successful",
                                    Toast.LENGTH_SHORT).show();

                            // Get current user ID
                            String userId = mAuth.getCurrentUser().getUid();

                            // Check user type in Firestore
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            db.collection("users").document(userId).get()
                                .addOnSuccessListener(documentSnapshot -> {
                                    if (documentSnapshot.exists()) {
                                        // Get user type
                                        String userType = documentSnapshot.getString("userType");
                                        Log.d("Firebase", "User type: " + userType);

                                        // Navigate based on user type
                                        Intent intent;
                                        if (userType != null && userType.equals("customer")) {
                                            intent = new Intent(HomeActivity.this, CustomerDashboardActivity.class);
                                        } else {
                                            intent = new Intent(HomeActivity.this, DashboardActivity.class);
                                        }
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Log.e("Firebase", "User document does not exist");
                                        Toast.makeText(HomeActivity.this, "User data not found",
                                                Toast.LENGTH_SHORT).show();

                                        // Default to DashboardActivity if user data not found
                                        Intent intent = new Intent(HomeActivity.this, DashboardActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                })
                                .addOnFailureListener(e -> {
                                    Log.e("Firebase", "Error getting user data", e);
                                    Toast.makeText(HomeActivity.this,
                                            "Error determining user type", Toast.LENGTH_SHORT).show();

                                    // Default to DashboardActivity on error
                                    Intent intent = new Intent(HomeActivity.this, DashboardActivity.class);
                                    startActivity(intent);
                                    finish();
                                });
                        } else {
                            // Sign in failed
                            Log.w("Firebase", "signInWithEmail:failure", task.getException());
                            Toast.makeText(HomeActivity.this, "Authentication failed: " +
                                    task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
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
    }

    // Password validation method
    private boolean isValidPassword(String password) {
        // Check password length (minimum 8 characters)
        if (password.length() < 8) {
            Toast.makeText(HomeActivity.this, "Password must be at least 8 characters long", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Check for at least one uppercase letter
        if (!password.matches(".*[A-Z].*")) {
            Toast.makeText(HomeActivity.this, "Password must contain at least one uppercase letter", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Check for at least one lowercase letter
        if (!password.matches(".*[a-z].*")) {
            Toast.makeText(HomeActivity.this, "Password must contain at least one lowercase letter", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Check for at least one digit
        if (!password.matches(".*\\d.*")) {
            Toast.makeText(HomeActivity.this, "Password must contain at least one number", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Check for at least one special character
        if (!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*")) {
            Toast.makeText(HomeActivity.this, "Password must contain at least one special character", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}