package com.example.demo;

public class ReviewData {
    private String username;
    private String reviewText;

    public ReviewData(String username, String reviewText) {
        this.username = username;
        this.reviewText = reviewText;
    }

    public String getUsername() {
        return username;
    }

    public String getReviewText() {
        return reviewText;
    }
}