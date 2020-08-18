package com.example.studentprofile;

public class ArticleItem {
    private String id;
    private String title;
    private String content;
    private String description;
    public ArticleItem(String id, String title, String content, String description){
        this.id = id;
        this.title = title;
        this.content = content;
        this.description=description;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getDescription() {
        return description;
    }
}
