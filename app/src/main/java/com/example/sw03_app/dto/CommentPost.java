package com.example.sw03_app.dto;

import com.google.gson.annotations.SerializedName;

public class CommentPost {

    @SerializedName("content")
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}