package com.example.app_techspehre_news;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.*;

public class UserProfileScreen extends AppCompatActivity {

    TextView usernameText, emailText;
    ImageView profileImage;
    Button editInfoButton, signOutButton;

    DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_info); // Your XML

        // UI References
        usernameText = findViewById(R.id.usernameText);
        emailText = findViewById(R.id.emailText);
        profileImage = findViewById(R.id.profileImage);
        editInfoButton = findViewById(R.id.editInfoButton);
        signOutButton = findViewById(R.id.signOutButton);

        // Get username from intent
        String username = getIntent().getStringExtra("username");
        if (username == null || username.isEmpty()) {
            Toast.makeText(this, "User not found!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Firebase reference
        dbRef = FirebaseDatabase.getInstance("https://newstechsphere-default-rtdb.firebaseio.com/")
                .getReference("users").child(username);

        // Fetch and display user info
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String email = snapshot.child("email").getValue(String.class);
                usernameText.setText("Username: " + username);
                emailText.setText("Email: " + email);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(UserProfileScreen.this, "Error loading profile", Toast.LENGTH_SHORT).show();
            }
        });

        // Sign Out Button
        signOutButton.setOnClickListener(v -> {
            Intent intent = new Intent(UserProfileScreen.this, SignInScreen.class);
            startActivity(intent);
            finish();
        });

        // Edit Info Button (Optional future logic)
        editInfoButton.setOnClickListener(v -> {
            Toast.makeText(this, "Edit feature coming soon!", Toast.LENGTH_SHORT).show();
        });
    }
}
