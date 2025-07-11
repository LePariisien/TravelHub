package com.example.TravelHub.Entities;

public class Session {
    private String id;
    private String sessionToken;
    private String userId;

    public Session() {}

    public Session(String id, String sessionToken, String userId) {
        this.id = id;
        this.sessionToken = sessionToken;
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
