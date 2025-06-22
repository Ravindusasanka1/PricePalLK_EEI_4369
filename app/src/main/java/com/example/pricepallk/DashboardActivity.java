package com.example.pricepallk;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);
        if (getSupportActionBar() != null) { // Hide the action bar
            getSupportActionBar().hide();
        }

        // Find the stats icon
        ImageView statsIcon = findViewById(R.id.imageView5);

        // Set click listener for the stats icon
        statsIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to StatsActivity
                Intent intent = new Intent(DashboardActivity.this, StatsActivity.class);
                startActivity(intent);
            }
        });

        // Find the Add products button
        Button addProductButton = findViewById(R.id.button7);

// Set click listener for Add products button
        addProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to AddproductActivity
                Intent intent = new Intent(DashboardActivity.this, AddproductActivity.class);
                startActivity(intent);
            }
        });

        // Find the Remove products button
        Button removeProductButton = findViewById(R.id.button8);

        // Set click listener for Remove products button
        removeProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // Navigate to DeleteProductActivity
                    Intent intent = new Intent(DashboardActivity.this, DeleteProductActivity.class);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // Find the Store Location button
        Button storeLocationButton = findViewById(R.id.button9);

// Set click listener for Store Location button
        storeLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to LocationActivity
                Intent intent = new Intent(DashboardActivity.this, LocationActivity.class);
                startActivity(intent);
            }
        });

        // Find the info button in your bottom navigation view
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.info) {
                // Navigate to InfoActivity when info menu item is clicked
                Intent intent = new Intent(DashboardActivity.this, InfoActivity.class);
                startActivity(intent);
                return true;
            }
            return false;
        });
    }
}