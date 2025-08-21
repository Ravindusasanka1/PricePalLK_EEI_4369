package com.example.pricepallk;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class ComparePriceActivity extends AppCompatActivity {

    private ImageView photoImageView;
    private TextView noPhotoText;
    private Uri selectedImageUri = null;
    private static final String TAG = "ComparePriceActivity";

    private final ActivityResultLauncher<Intent> pickImageLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Log.d(TAG, "Image picker result received: " + result.getResultCode());
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    selectedImageUri = result.getData().getData();
                    Log.d(TAG, "Selected image URI: " + selectedImageUri);
                    if (selectedImageUri != null) {
                        try {
                            photoImageView.setImageURI(null); // Clear previous image
                            photoImageView.setImageURI(selectedImageUri);
                            noPhotoText.setVisibility(View.GONE);
                            Toast.makeText(this, "Photo selected successfully", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Log.e(TAG, "Error setting image URI: " + e.getMessage(), e);
                            Toast.makeText(this, "Error displaying image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Log.d(TAG, "No image selected or result not OK");
                    Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_compare_price);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Initialize views
        photoImageView = findViewById(R.id.photoImageView);
        noPhotoText = findViewById(R.id.noPhotoText);

        Log.d(TAG, "Activity created, checking for action");

        // Check if we were launched with a specific action
        String action = getIntent().getStringExtra("action");
        if (action != null) {
            Log.d(TAG, "Action received: " + action);
            if (action.equals("add_photo")) {
                openGallery();
            } else if (action.equals("remove_photo")) {
                removePhoto();
            }
        }
    }

    private void openGallery() {
        try {
            Log.d(TAG, "Opening gallery...");
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            pickImageLauncher.launch(intent);
            Toast.makeText(this, "Opening photo picker...", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "Error opening gallery: " + e.getMessage(), e);
            Toast.makeText(this, "Error opening gallery: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void removePhoto() {
        photoImageView.setImageDrawable(null);
        selectedImageUri = null;
        noPhotoText.setVisibility(View.VISIBLE);
        Toast.makeText(this, "Photo removed", Toast.LENGTH_SHORT).show();
    }
}