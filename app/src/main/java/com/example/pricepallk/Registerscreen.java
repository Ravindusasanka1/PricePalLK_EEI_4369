package com.example.pricepallk;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Registerscreen extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText emailField, passwordField;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registerscreen);
        if (getSupportActionBar() != null) { // Hide the action bar
            getSupportActionBar().hide();
        }


        mAuth = FirebaseAuth.getInstance();

        // Initialize UI elements
        emailField = findViewById(R.id.editTextTextEmailAddress3);
        passwordField = findViewById(R.id.editTextTextPassword);
        registerButton = findViewById(R.id.button3);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Registerscreen", "Register button clicked");
                // Get all input values
                EditText nameField = findViewById(R.id.editTextText);
                String name = nameField.getText().toString().trim();
                String email = emailField.getText().toString().trim();
                String password = passwordField.getText().toString().trim();
                Log.d("Registerscreen", "Name: " + name + ", Email: " + email);

                // Validate name
                if (name.isEmpty()) {
                    Toast.makeText(Registerscreen.this, "Please enter your name", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Validate email and password
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(Registerscreen.this, "Please enter both email and password", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Password strength validation
                if (!isValidPassword(password)) {
                    return;
                }

                // Get confirm password
                EditText confirmPasswordField = findViewById(R.id.editTextTextPassword2);
                String confirmPassword = confirmPasswordField.getText().toString().trim();

                // Check if passwords match
                if (!password.equals(confirmPassword)) {
                    Toast.makeText(Registerscreen.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }

                RadioGroup radioGroupUserType = findViewById(R.id.radioGroupUserType);
                int selectedRadioButtonId = radioGroupUserType.getCheckedRadioButtonId();

                // Check if user selected a type
                if (selectedRadioButtonId == -1) {
                    Toast.makeText(Registerscreen.this, "Please select user type", Toast.LENGTH_SHORT).show();
                    return;
                }

                String userType = "";
                if (selectedRadioButtonId == R.id.radioButtonRetailer) {
                    userType = "retailer";
                } else if (selectedRadioButtonId == R.id.radioButtonCustomer) {
                    userType = "customer";
                }
                final String finalUserType = userType;

                // Create user with email and password
                Log.d("Firebase", "Attempting to create user: " + email);
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(Registerscreen.this, task -> {
                            if (task.isSuccessful()) {
                                // Sign up success
                                Log.d("Firebase", "createUserWithEmail:success");

                                // Store additional user info in Firestore
                                FirebaseUser user = mAuth.getCurrentUser();
                                String uid = user.getUid();
                                Log.d("Firebase", "User created with UID: " + uid + ". Saving to Firestore...");

                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                Map<String, Object> userInfo = new HashMap<>();
                                userInfo.put("name", name);
                                userInfo.put("email", email);
                                userInfo.put("createdAt", new Date());
                                userInfo.put("userType", finalUserType);

                                db.collection("users").document(uid).set(userInfo)
                                        .addOnSuccessListener(aVoid -> {
                                            Log.d("Firebase", "User document successfully written to Firestore");
                                            Toast.makeText(Registerscreen.this, "Registration successful", Toast.LENGTH_SHORT).show();

                                            // Navigate based on user type
                                            Intent intent;
                                            if (finalUserType.equals("customer")) {
                                                intent = new Intent(Registerscreen.this, CustomerDashboardActivity.class);
                                            } else {
                                                intent = new Intent(Registerscreen.this, DashboardActivity.class);
                                            }
                                            startActivity(intent);
                                            finish();
                                        })
                                        .addOnFailureListener(e -> {
                                            Log.e("Firebase", "Error writing user document to Firestore", e);
                                            Toast.makeText(Registerscreen.this, "Failed to save user data: " + e.getMessage(),
                                                    Toast.LENGTH_SHORT).show();
                                        });
                            } else {
                                // Sign up failed
                                Log.e("Firebase", "createUserWithEmail:failure", task.getException());
                                Toast.makeText(Registerscreen.this, "Registration failed: " +
                                        task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        TextView backToLoginTextView = findViewById(R.id.textViewBackToLoginRegister);
        backToLoginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Registerscreen.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


    private boolean isValidPassword(String password) {
        // Check password length (minimum 8 characters)
        if (password.length() < 8) {
            Toast.makeText(Registerscreen.this, "Password must be at least 8 characters long", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Check for at least one uppercase letter
        if (!password.matches(".*[A-Z].*")) {
            Toast.makeText(Registerscreen.this, "Password must contain at least one uppercase letter", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Check for at least one lowercase letter
        if (!password.matches(".*[a-z].*")) {
            Toast.makeText(Registerscreen.this, "Password must contain at least one lowercase letter", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Check for at least one digit
        if (!password.matches(".*\\d.*")) {
            Toast.makeText(Registerscreen.this, "Password must contain at least one number", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Check for at least one special character
        if (!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*")) {
            Toast.makeText(Registerscreen.this, "Password must contain at least one special character", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
