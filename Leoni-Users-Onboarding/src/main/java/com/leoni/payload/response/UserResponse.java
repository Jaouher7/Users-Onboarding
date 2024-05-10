package com.leoni.payload.response;

import java.util.List;

public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private List<String> roles;
    private byte[] imageData;

    public UserResponse(Long id, String username, String email, List<String> roles, byte[] imageData) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
        this.imageData = imageData;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }
}
