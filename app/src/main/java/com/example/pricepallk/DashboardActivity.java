package com.example.pricepallk;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);
        if (getSupportActionBar() != null) { // Hide the action bar
            getSupportActionBar().hide();
        }

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

        // Find the Update Price button (button4)
        Button updatePriceButton = findViewById(R.id.button4);

        // Set click listener for Update Price button
        updatePriceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to ComparePriceActivity with add_photo action
                Intent intent = new Intent(DashboardActivity.this, ComparePriceActivity.class);
                intent.putExtra("action", "add_photo");
                startActivity(intent);
            }
        });

        // Find the Remove Price button (button5)
        Button removePriceButton = findViewById(R.id.button5);

        // Set click listener for Remove Price button
        removePriceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to ComparePriceActivity with remove_photo action
                Intent intent = new Intent(DashboardActivity.this, ComparePriceActivity.class);
                intent.putExtra("action", "remove_photo");
                startActivity(intent);
            }
        });
    }
}