package com.example.pricepallk;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class ViewProductsActivity extends AppCompatActivity {
    private static final String TAG = "ViewProductsActivity";

    // Firebase
    private FirebaseFirestore db;

    // UI Components
    private RecyclerView recyclerView;
    private EditText searchEditText;
    private ProgressBar progressBar;
    private LinearLayout emptyStateLayout;

    // Data
    private ProductAdapter adapter;
    private List<Product> productList;
    private List<Product> filteredList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_products);

        // Hide action bar if present
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Initialize Firebase
        db = FirebaseFirestore.getInstance();

        // Initialize UI components
        recyclerView = findViewById(R.id.recyclerViewProducts);
        searchEditText = findViewById(R.id.editTextSearch);
        progressBar = findViewById(R.id.progressBar);
        emptyStateLayout = findViewById(R.id.layoutEmptyState);

        // Setup RecyclerView
        productList = new ArrayList<>();
        filteredList = new ArrayList<>();
        adapter = new ProductAdapter(filteredList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Load products from Firestore
        loadProducts();

        // Setup search functionality
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                filterProducts(s.toString());
            }
        });
    }

    private void loadProducts() {
        // Show loading state
        progressBar.setVisibility(View.VISIBLE);
        emptyStateLayout.setVisibility(View.GONE);

        // Query Firestore for all products, ordered by name
        db.collection("products")
                .orderBy("productName", Query.Direction.ASCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    progressBar.setVisibility(View.GONE);
                    productList.clear();
                    filteredList.clear();

                    if (queryDocumentSnapshots.isEmpty()) {
                        emptyStateLayout.setVisibility(View.VISIBLE);
                        return;
                    }

                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        String id = document.getId();
                        String name = document.getString("productName");
                        String retailer = document.getString("retailerName");
                        Double price = document.getDouble("price");

                        if (name != null && retailer != null && price != null) {
                            Product product = new Product(id, name, retailer, price);
                            productList.add(product);
                            filteredList.add(product);
                        }
                    }

                    if (filteredList.isEmpty()) {
                        emptyStateLayout.setVisibility(View.VISIBLE);
                    }

                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    emptyStateLayout.setVisibility(View.VISIBLE);
                    Log.e(TAG, "Error loading products", e);
                });
    }

    private void filterProducts(String query) {
        filteredList.clear();

        if (query.isEmpty()) {
            filteredList.addAll(productList);
        } else {
            String lowerCaseQuery = query.toLowerCase();
            for (Product product : productList) {
                if (product.name.toLowerCase().contains(lowerCaseQuery) ||
                    product.retailer.toLowerCase().contains(lowerCaseQuery)) {
                    filteredList.add(product);
                }
            }
        }

        adapter.notifyDataSetChanged();

        if (filteredList.isEmpty()) {
            emptyStateLayout.setVisibility(View.VISIBLE);
        } else {
            emptyStateLayout.setVisibility(View.GONE);
        }
    }

    // Product model class
    private static class Product {
        String id;
        String name;
        String retailer;
        double price;

        Product(String id, String name, String retailer, double price) {
            this.id = id;
            this.name = name;
            this.retailer = retailer;
            this.price = price;
        }
    }

    // RecyclerView Adapter with programmatically created item views
    private class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
        private List<Product> products;

        ProductAdapter(List<Product> products) {
            this.products = products;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // Create CardView programmatically
            CardView cardView = new CardView(parent.getContext());
            cardView.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            cardView.setRadius(dpToPx(8));
            cardView.setCardElevation(dpToPx(2));
            cardView.setUseCompatPadding(true);

            // Create LinearLayout for card content
            LinearLayout layout = new LinearLayout(parent.getContext());
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setPadding(dpToPx(16), dpToPx(16), dpToPx(16), dpToPx(16));
            cardView.addView(layout);

            // Create TextViews for product info
            TextView productNameTextView = new TextView(parent.getContext());
            productNameTextView.setId(View.generateViewId());
            productNameTextView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            productNameTextView.setTextSize(18);
            productNameTextView.setTextColor(Color.BLACK);
            productNameTextView.setTypeface(null, android.graphics.Typeface.BOLD);
            layout.addView(productNameTextView);

            TextView retailerNameTextView = new TextView(parent.getContext());
            retailerNameTextView.setId(View.generateViewId());
            retailerNameTextView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            retailerNameTextView.setTextSize(14);
            LinearLayout.LayoutParams retailerParams = (LinearLayout.LayoutParams) retailerNameTextView.getLayoutParams();
            retailerParams.topMargin = dpToPx(4);
            retailerNameTextView.setLayoutParams(retailerParams);
            layout.addView(retailerNameTextView);

            TextView priceTextView = new TextView(parent.getContext());
            priceTextView.setId(View.generateViewId());
            priceTextView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            priceTextView.setTextSize(16);
            priceTextView.setTextColor(Color.parseColor("#4CAF50"));
            priceTextView.setTypeface(null, android.graphics.Typeface.BOLD);
            LinearLayout.LayoutParams priceParams = (LinearLayout.LayoutParams) priceTextView.getLayoutParams();
            priceParams.topMargin = dpToPx(4);
            priceTextView.setLayoutParams(priceParams);
            layout.addView(priceTextView);

            return new ViewHolder(cardView, productNameTextView, retailerNameTextView, priceTextView);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Product product = products.get(position);
            holder.productNameTextView.setText(product.name);
            holder.retailerNameTextView.setText("Retailer: " + product.retailer);
            holder.priceTextView.setText("Rs. " + product.price);
        }

        @Override
        public int getItemCount() {
            return products.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView productNameTextView;
            TextView retailerNameTextView;
            TextView priceTextView;

            ViewHolder(View itemView, TextView productNameTextView, TextView retailerNameTextView, TextView priceTextView) {
                super(itemView);
                this.productNameTextView = productNameTextView;
                this.retailerNameTextView = retailerNameTextView;
                this.priceTextView = priceTextView;
            }
        }
    }

    // Utility method to convert dp to pixels
    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
}