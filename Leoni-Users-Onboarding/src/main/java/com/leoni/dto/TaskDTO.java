package com.leoni.dto;

public class TaskDTO {
        private String username;

        private Long designId;

        private String state;

        private String priority;

        public TaskDTO() {
        }

        public TaskDTO(String username, Long designId, String state, String priority) {
            this.username = username;
            this.designId = designId;
            this.state = state;
            this.priority = priority;
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
