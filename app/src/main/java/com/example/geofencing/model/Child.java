package com.example.geofencing.model;

public class Child {
    private String username;
    private String email;
    private String childId;

    public Child() {
    }
    public Child(String username, String email, String childId) {
        this.username = username;
        this.email = email;
        this.childId = childId;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getChildId() {
        return childId;
    }
}
