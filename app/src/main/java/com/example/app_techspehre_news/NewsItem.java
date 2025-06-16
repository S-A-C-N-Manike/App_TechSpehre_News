package com.example.app_techspehre_news;

public class NewsItem {
    public String title;
    public String description;
    public String imageUrl;
    public String category;

    public NewsItem() {} // required for Firebase

    public NewsItem(String title, String description, String imageUrl, String category) {
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.category = category;
    }
}
