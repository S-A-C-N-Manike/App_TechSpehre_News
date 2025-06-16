package com.example.app_techspehre_news;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.firebase.database.*;

public class UserProfileScreen extends AppCompatActivity {

    TextView usernameText, emailText;
    ImageView profileImage;
    ImageButton backButton;
    AppCompatButton editInfoButton, signOutButton;

    String currentUsername, currentEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_info); // Your XML should include signOutButton

        // 🔗 Link UI elements
        usernameText = findViewById(R.id.usernameText);
        emailText = findViewById(R.id.emailText);
        profileImage = findViewById(R.id.profileImage);
        backButton = findViewById(R.id.backButton);
        editInfoButton = findViewById(R.id.editInfoButton);
        signOutButton = findViewById(R.id.exitButton);

        // 🔐 Get logged-in username
        currentUsername = getIntent().getStringExtra("username");

        if (currentUsername == null || currentUsername.isEmpty()) {
            Toast.makeText(this, "Username not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // 🔄 Load user info
        DatabaseReference userRef = FirebaseDatabase.getInstance("https://newstechsphere-default-rtdb.firebaseio.com/")
                .getReference("users").child(currentUsername);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currentEmail = snapshot.child("email").getValue(String.class);
                usernameText.setText("Username: " + currentUsername);
                emailText.setText("Email: " + currentEmail);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserProfileScreen.this, "Failed to load profile", Toast.LENGTH_SHORT).show();
            }
        });

        // ⬅️ Back to NewsScreen
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(UserProfileScreen.this, NewsScreen.class);
            intent.putExtra("username", currentUsername);
            startActivity(intent);
            finish();
        });

        // ✏️ Edit Info
        editInfoButton.setOnClickListener(v -> showEditDialog(currentUsername));

        // 🔐 Sign Out confirmation
        signOutButton.setOnClickListener(v -> showSignOutDialog());
    }

    // 🔧 Edit dialog method
    private void showEditDialog(String currentUsername) {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_edit_user_info, null);

        EditText editUsername = dialogView.findViewById(R.id.editUsername);
        EditText editEmail = dialogView.findViewById(R.id.editEmail);
        AppCompatButton btnConfirm = dialogView.findViewById(R.id.btnConfirmEdit);
        AppCompatButton btnCancel = dialogView.findViewById(R.id.btnCancelEdit);

        editUsername.setText(currentUsername);

        DatabaseReference userRef = FirebaseDatabase.getInstance("https://newstechsphere-default-rtdb.firebaseio.com/")
                .getReference("users").child(currentUsername);

        userRef.child("email").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String currentEmail = snapshot.getValue(String.class);
                editEmail.setText(currentEmail);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserProfileScreen.this, "Failed to load email", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .setCancelable(false)
                .create();

        btnConfirm.setOnClickListener(v -> {
            String newUsername = editUsername.getText().toString().trim();
            String newEmail = editEmail.getText().toString().trim();

            if (newUsername.isEmpty() || newEmail.isEmpty()) {
                Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            userRef.child("email").setValue(newEmail);

            if (!newUsername.equals(currentUsername)) {
                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String password = snapshot.child("password").getValue(String.class);

                        if (password == null) {
                            Toast.makeText(UserProfileScreen.this, "Password missing!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        DatabaseReference newRef = FirebaseDatabase.getInstance("https://newstechsphere-default-rtdb.firebaseio.com/")
                                .getReference("users").child(newUsername);

                        newRef.child("email").setValue(newEmail);
                        newRef.child("password").setValue(password);

                        userRef.removeValue().addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(UserProfileScreen.this, "Info updated!", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                Intent i = new Intent(UserProfileScreen.this, UserProfileScreen.class);
                                i.putExtra("username", newUsername);
                                startActivity(i);
                                finish();
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(UserProfileScreen.this, "Error updating username", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(this, "Email updated!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    //  Sign Out Confirmation Dialog
    private void showSignOutDialog() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_sign_out_confirmation, null);

        AppCompatButton btnYes = dialogView.findViewById(R.id.btnYesSignOut);
        AppCompatButton btnNo = dialogView.findViewById(R.id.btnNoSignOut);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .setCancelable(false)
                .create();

        btnYes.setOnClickListener(v -> {
            Intent intent = new Intent(UserProfileScreen.this, SignInScreen.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // prevent back navigation
            startActivity(intent);
            dialog.dismiss();
            finish();
        });

        btnNo.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }
}
