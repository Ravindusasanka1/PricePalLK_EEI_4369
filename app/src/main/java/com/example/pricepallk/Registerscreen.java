package com.example.pricepallk;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Paint;
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

public class Registerscreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registerscreen);

        if (getSupportActionBar() != null) { // Hide the action bar
            getSupportActionBar().hide();
        }
        // Setup for "Back to Login" TextView
        TextView backToLoginTextView = findViewById(R.id.textViewBackToLoginRegister);
        backToLoginTextView.setPaintFlags(backToLoginTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        backToLoginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to login screen
                Intent intent = new Intent(Registerscreen.this, HomeActivity.class);
                startActivity(intent);
                finish(); // Close this activity
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Setup for "Create Account" Button
        Button createAccountButton = findViewById(R.id.button3);
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get all registration details
                EditText nameField = findViewById(R.id.editTextText);
                EditText emailField = findViewById(R.id.editTextTextEmailAddress3);
                EditText passwordField = findViewById(R.id.editTextTextPassword);
                EditText confirmPasswordField = findViewById(R.id.editTextTextPassword2);

                String name = nameField.getText().toString().trim();
                String email = emailField.getText().toString().trim();
                String password = passwordField.getText().toString().trim();
                String confirmPassword = confirmPasswordField.getText().toString().trim();

                // Validate inputs
                if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(Registerscreen.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!password.equals(confirmPassword)) {
                    Toast.makeText(Registerscreen.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Initialize Firebase if needed
                if (FirebaseApp.getApps(Registerscreen.this).isEmpty()) {
                    FirebaseApp.initializeApp(Registerscreen.this);
                }

                // Get Firestore instance
                FirebaseFirestore db = FirebaseFirestore.getInstance();

                // Create a map with the registration data
                Map<String, Object> user = new HashMap<>();
                user.put("name", name);
                user.put("email", email);
                user.put("password", password); // Note: In production, use Firebase Auth instead
                user.put("registrationDate", new Date());

                // Save to Firestore
                db.collection("users")
                    .add(user)
                    .addOnSuccessListener(documentReference -> {
                        Log.d("Firestore", "User registered with ID: " + documentReference.getId());
                        Toast.makeText(Registerscreen.this, "Registration successful", Toast.LENGTH_SHORT).show();

                        // Navigate to dashboard screen
                        Intent intent = new Intent(Registerscreen.this, DashboardActivity.class);
                        startActivity(intent);
                        finish(); // Close this activity
                    })
                    .addOnFailureListener(e -> {
                        Log.w("Firestore", "Error registering user", e);
                        Toast.makeText(Registerscreen.this, "Registration failed: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                    });
            }
        });
    }
}
