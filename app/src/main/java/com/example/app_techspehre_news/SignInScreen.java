package com.example.app_techspehre_news;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SignInScreen extends AppCompatActivity {

    EditText usernameEditText, passwordEditText;
    Button loginButton;
    TextView signupLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        signupLink = findViewById(R.id.signupLink);

        loginButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Logged in successfully", Toast.LENGTH_SHORT).show();
            }
        });

        signupLink.setOnClickListener(v -> {
            startActivity(new Intent(SignInScreen.this, SignUpScreen.class));
        });
    }
}
