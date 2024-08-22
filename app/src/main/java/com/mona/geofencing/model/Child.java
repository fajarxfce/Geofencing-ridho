package com.mona.geofencing.model;

public class Child {
    private String username;
    private String email;
    private String childId;
    private String pairCode;

    public Child() {
    }

    public Child(String username, String email, String childId, String pairCode) {
        this.username = username;
        this.email = email;
        this.childId = childId;
        this.pairCode = pairCode;
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

    public String getPairCode() {
        return pairCode;
    }
}
