package com.udacity.jwdnd.course1.cloudstorage.model;

public class Note {
    private Integer id;
    private String title;
    private String description;
    private Integer userId;

    public Note(String title, String description, Integer userId) {
        this.title = title;
        this.description = description;
        this.userId = userId;
    }

    public Note() {}

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getDescription() {
        return description;
    }
}
