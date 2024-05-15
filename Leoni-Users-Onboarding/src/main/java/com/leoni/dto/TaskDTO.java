package com.leoni.dto;

public class TaskDTO {
    private Long id;
    private String username;
    private Long designId;
    private String state;
    private String priority;

    public TaskDTO() {
    }

    public TaskDTO(Long id, String username, Long designId, String state, String priority) {
        this.id = id;
        this.username = username;
        this.designId = designId;
        this.state = state;
        this.priority = priority;
    }

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

    public Long getDesignId() {
        return designId;
    }

    public void setDesignId(Long designId) {
        this.designId = designId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}
