package com.example.pricepallk;

import android.content.Intent;
import android.graphics.Paint;
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
import com.google.android.material.snackbar.Snackbar;
import android.graphics.Color;


public class ForgotpasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgotpassword);
        if (getSupportActionBar() != null) { // Hide the action bar
            getSupportActionBar().hide();
        }

        // Setup for "Forgot password?" TextView
        TextView backToLoginTextView = findViewById(R.id.textViewBackToLogin);
        backToLoginTextView.setPaintFlags(backToLoginTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        backToLoginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to forgot password screen
                Intent intent = new Intent(ForgotpasswordActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        // Get references to email field and reset button
        EditText emailField = findViewById(R.id.editTextTextEmailAddress);
        Button resetButton = findViewById(R.id.button2);

        // Set click listener for reset button
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailField.getText().toString().trim();

                if (email.isEmpty()) {
                    Snackbar.make(v, "Please enter your email address", Snackbar.LENGTH_SHORT)
                            .setBackgroundTint(Color.parseColor("#333333"))
                            .setTextColor(Color.WHITE)
                            .show();
                } else {
                    Snackbar.make(v, "Reset link sent", Snackbar.LENGTH_SHORT)
                            .setBackgroundTint(Color.parseColor("#333333"))
                            .setTextColor(Color.WHITE)
                            .show();
                }
            }
        });



    }
}

