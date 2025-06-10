package com.example.app_techspehre_news;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpScreen extends AppCompatActivity {

    EditText signupUsername, signupEmail, signupPassword, signupConfirmPassword;
    Button signupButton;
    TextView loginLink;
    DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        signupUsername = findViewById(R.id.signupUsername);
        signupEmail = findViewById(R.id.signupEmail);
        signupPassword = findViewById(R.id.signupPassword);
        signupConfirmPassword = findViewById(R.id.signupConfirmPassword);
        signupButton = findViewById(R.id.signupButton);
        loginLink = findViewById(R.id.loginLink);

        // Connect to Firebase path /users
        dbRef = FirebaseDatabase.getInstance("https://newstechsphere-default-rtdb.firebaseio.com/")
                .getReference("users");

        signupButton.setOnClickListener(v -> {
            String username = signupUsername.getText().toString().trim();
            String email = signupEmail.getText().toString().trim();
            String password = signupPassword.getText().toString().trim();
            String confirmPassword = signupConfirmPassword.getText().toString().trim();

            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else if (!password.equals(confirmPassword)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            } else if (!isValidPassword(password)) {
                Toast.makeText(this, "Password must be 8+ chars with uppercase, lowercase, number & symbol", Toast.LENGTH_LONG).show();
            } else {
                dbRef.child(username).child("email").setValue(email);
                dbRef.child(username).child("password").setValue(password);
                Toast.makeText(this, "Account created successfully!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, SignInScreen.class));
                finish();
            }
        });

        loginLink.setOnClickListener(v -> {
            startActivity(new Intent(this, SignInScreen.class));
            finish();
        });
    }

    private boolean isValidPassword(String password) {
        String pattern = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$";
        return password.matches(pattern);
    }
}
