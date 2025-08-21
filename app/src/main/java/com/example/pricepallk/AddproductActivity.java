package com.example.pricepallk;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddproductActivity extends AppCompatActivity {
    private EditText productNameEditText, retailerNameEditText, priceEditText;
    private Button addProductButton;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addproduct);

        // Initialize Firebase instances
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Initialize UI elements
        productNameEditText = findViewById(R.id.editTextTextEmailAddress5);
        retailerNameEditText = findViewById(R.id.editTextTextEmailAddress6);
        priceEditText = findViewById(R.id.editTextTextEmailAddress2);
        addProductButton = findViewById(R.id.button9);

        // Change button text from "Button" to "Add Product"
        addProductButton.setText("Add Product");

        // Set click listener for add product button
        addProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProductToFirestore();
            }
        });
    }

    private void saveProductToFirestore() {
        // Get values from input fields
        String productName = productNameEditText.getText().toString().trim();
        String retailerName = retailerNameEditText.getText().toString().trim();
        String priceString = priceEditText.getText().toString().trim();

        // Validate input fields
        if (productName.isEmpty()) {
            productNameEditText.setError("Product name is required");
            productNameEditText.requestFocus();
            return;
        }

        if (retailerName.isEmpty()) {
            retailerNameEditText.setError("Retailer name is required");
            retailerNameEditText.requestFocus();
            return;
        }

        if (priceString.isEmpty()) {
            priceEditText.setError("Price is required");
            priceEditText.requestFocus();
            return;
        }

        // Try to parse the price as a double
        double price;
        try {
            price = Double.parseDouble(priceString);
        } catch (NumberFormatException e) {
            priceEditText.setError("Please enter a valid price");
            priceEditText.requestFocus();
            return;
        }

        // Show progress indication to user
        Toast.makeText(AddproductActivity.this, "Adding product...", Toast.LENGTH_SHORT).show();

        // Get current user ID
        String userId = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : "unknown";

        // Create product data map
        Map<String, Object> product = new HashMap<>();
        product.put("productName", productName);
        product.put("retailerName", retailerName);
        product.put("price", price);
        product.put("retailerId", userId);
        product.put("timestamp", System.currentTimeMillis());

        // Save to Firestore
        db.collection("products")
                .add(product)
                .addOnSuccessListener(documentReference -> {
                    Log.d("AddProduct", "Product added with ID: " + documentReference.getId());
                    Toast.makeText(AddproductActivity.this, "Product added successfully", Toast.LENGTH_SHORT).show();

                    // Clear form fields
                    productNameEditText.setText("");
                    retailerNameEditText.setText("");
                    priceEditText.setText("");
                })
                .addOnFailureListener(e -> {
                    Log.e("AddProduct", "Error adding product", e);
                    Toast.makeText(AddproductActivity.this, "Failed to add product: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }
}