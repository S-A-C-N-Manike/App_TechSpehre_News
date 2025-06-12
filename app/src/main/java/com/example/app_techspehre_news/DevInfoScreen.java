package com.example.app_techspehre_news;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class DevInfoScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer_info); // Make sure this XML exists

        // Reference the Exit button
        Button exitButton = findViewById(R.id.exitButton);

        // Exit to NewsScreen
        exitButton.setOnClickListener(v -> {
            Intent intent = new Intent(DevInfoScreen.this, NewsScreen.class);
            startActivity(intent);
            finish(); // Clears DeveloperInfoScreen from back stack
        });
    }
}
