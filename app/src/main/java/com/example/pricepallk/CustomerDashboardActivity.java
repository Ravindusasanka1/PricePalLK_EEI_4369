package com.example.pricepallk;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CustomerDashboardActivity extends AppCompatActivity {
    private Button btnComparePrice, btnViewProducts, btnStoreLocations, btnFeedback, btnInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_customer_dashboard);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize all buttons
        btnComparePrice = findViewById(R.id.btnComparePrice);
        btnViewProducts = findViewById(R.id.btnViewProducts);
        btnStoreLocations = findViewById(R.id.btnStoreLocations);
        btnFeedback = findViewById(R.id.btnFeedback);
        btnInfo = findViewById(R.id.button6); // ID from layout is button6 for Info button

        // Set click listeners for each button
        btnComparePrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CustomerDashboardActivity.this, ComparePriceActivity.class));
            }
        });

        btnViewProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CustomerDashboardActivity.this, ViewProductsActivity.class));
            }
        });

        btnStoreLocations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CustomerDashboardActivity.this, LocationActivity.class));
            }
        });

        btnFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CustomerDashboardActivity.this, FeedbackActivity.class));
            }
        });

        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CustomerDashboardActivity.this, InfoActivity.class));
            }
        });
    }
}