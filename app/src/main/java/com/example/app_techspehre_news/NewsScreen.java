package com.example.app_techspehre_news;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NewsScreen extends AppCompatActivity {

    RecyclerView newsRecyclerView;
    EditText searchBar;
    Button btnAcademics, btnSports, btnEvents;

    List<NewsItem> fullList = new ArrayList<>();
    List<NewsItem> filteredList = new ArrayList<>();
    NewsAdapter adapter;
    DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_screen); // Make sure this layout file exists

        // Initialize UI components
        newsRecyclerView = findViewById(R.id.newsRecyclerView);
        searchBar = findViewById(R.id.searchBar);
        btnAcademics = findViewById(R.id.btnAcademics);
        btnSports = findViewById(R.id.btnSports);
        btnEvents = findViewById(R.id.btnEvents);

        // Setup RecyclerView
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NewsAdapter(filteredList, this); // Use 'this' if NewsAdapter needs Context
        newsRecyclerView.setAdapter(adapter);

        // Connect to Firebase
        dbRef = FirebaseDatabase.getInstance("https://newstechsphere-default-rtdb.firebaseio.com/")
                .getReference("news");

        // Load all news from Firebase
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                fullList.clear();
                for (DataSnapshot newsSnap : snapshot.getChildren()) {
                    NewsItem item = newsSnap.getValue(NewsItem.class);
                    if (item != null) {
                        fullList.add(item);
                    }
                }
                filteredList.clear();
                filteredList.addAll(fullList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(NewsScreen.this, "Error loading news", Toast.LENGTH_SHORT).show();
            }
        });

        // Search filter
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterBySearch(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        // Filter buttons
        btnAcademics.setOnClickListener(v -> filterByCategory("Academics"));
        btnSports.setOnClickListener(v -> filterByCategory("Sports"));
        btnEvents.setOnClickListener(v -> filterByCategory("Events"));
    }

    private void filterBySearch(String query) {
        filteredList.clear();
        for (NewsItem item : fullList) {
            if (item.title != null && item.title.toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(item);
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void filterByCategory(String category) {
        filteredList.clear();
        for (NewsItem item : fullList) {
            if (item.category != null && item.category.equalsIgnoreCase(category)) {
                filteredList.add(item);
            }
        }
        adapter.notifyDataSetChanged();
    }
}
