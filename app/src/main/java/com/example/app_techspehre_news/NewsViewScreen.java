package com.example.app_techspehre_news;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;

public class NewsViewScreen extends AppCompatActivity {

    TextView newsTitle, newsDescription;
    ImageView newsImage;
    ImageButton backButton, profileButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_view_screen);

        // View initialization
        newsTitle = findViewById(R.id.newsTitle);
        newsDescription = findViewById(R.id.newsDescription);
        newsImage = findViewById(R.id.newsImage);
        backButton = findViewById(R.id.backButton);
       // profileButton = findViewById(R.id.profileButton);

        // Get data passed from NewsAdapter
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String description = intent.getStringExtra("description");
        String imageUrl = intent.getStringExtra("imageUrl");

        // Display content
        newsTitle.setText(title);
        newsDescription.setText(description);

        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(this).load(imageUrl).into(newsImage);
        } else {
            newsImage.setImageResource(R.drawable.placeholder);
        }

        // Navigate back to NewsScreen
        backButton.setOnClickListener(v -> {
            Intent backIntent = new Intent(NewsViewScreen.this, NewsScreen.class);
            startActivity(backIntent);
            finish();
        });

        // Navigate to Profile Screen
        //profileButton.setOnClickListener(v -> {
           // Intent profileIntent = new Intent(NewsViewScreen.this, UserProfileScreen.class);
            //startActivity(profileIntent);
       // });
    }
}
