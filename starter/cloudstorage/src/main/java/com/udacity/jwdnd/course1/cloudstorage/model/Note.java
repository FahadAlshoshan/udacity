package com.udacity.jwdnd.course1.cloudstorage.model;

public class Note {
    private Integer id;
    private String title;
    private String description;
    private Integer userId;

    public Note() {}

    public Note(String title, String description, Integer userId) {
        this.title = title;
        this.description = description;
        this.userId = userId;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

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
