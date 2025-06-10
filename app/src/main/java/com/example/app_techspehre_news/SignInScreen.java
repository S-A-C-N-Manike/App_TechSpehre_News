package com.example.app_techspehre_news;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.*;

public class SignInScreen extends AppCompatActivity {

    EditText usernameEditText, passwordEditText;
    Button loginButton;
    TextView signupLink;
    DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        signupLink = findViewById(R.id.signupLink);

        dbRef = FirebaseDatabase.getInstance("https://newstechsphere-default-rtdb.firebaseio.com/")
                .getReference("users");

        loginButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show();
                return;
            }

            dbRef.child(username).child("password").addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                String storedPassword = snapshot.getValue(String.class);
                                if (password.equals(storedPassword)) {
                                    Toast.makeText(SignInScreen.this, "Login successful!", Toast.LENGTH_SHORT).show();
                                    // TODO: Navigate to main app screen
                                } else {
                                    Toast.makeText(SignInScreen.this, "Incorrect password", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(SignInScreen.this, "User not found", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(SignInScreen.this, "Database error", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        signupLink.setOnClickListener(v -> {
            startActivity(new Intent(this, SignUpScreen.class));
        });
    }
}
