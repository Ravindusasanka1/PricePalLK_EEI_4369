package com.example.pricepallk;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class StatsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_stats); // Add this line to connect to the layout
        if (getSupportActionBar() != null) { // Hide the action bar
            getSupportActionBar().hide();
        }
    }
}