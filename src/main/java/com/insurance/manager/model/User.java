package com.insurance.manager.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "users")
public class User {

    @Id
    private String id;

    private String email;

    private String name;

    @Transient
    private List<com.insurance.manager.model.Document> documents;

    // Encrypted password for form login
    private String password;

    private String role; // e.g., ROLE_USER

    @org.springframework.data.mongodb.core.mapping.Field("avatar_url")
    private String avatarUrl;

    @org.springframework.data.mongodb.core.mapping.Field("created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public User() {
    }

    public User(String id, String email, String name, List<com.insurance.manager.model.Document> documents,
            String password, String role, String avatarUrl, LocalDateTime createdAt) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.documents = documents;
        this.password = password;
        this.role = role;
        this.avatarUrl = avatarUrl;
        this.createdAt = createdAt != null ? createdAt : LocalDateTime.now();
    }

    public static UserBuilder builder() {
        return new UserBuilder();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<com.insurance.manager.model.Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<com.insurance.manager.model.Document> documents) {
        this.documents = documents;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public static class UserBuilder {
        private String id;
        private String email;
        private String name;
        private List<com.insurance.manager.model.Document> documents;
        private String password;
        private String role;
        private String avatarUrl;
        private LocalDateTime createdAt;

        UserBuilder() {
        }

        public UserBuilder id(String id) {
            this.id = id;
            return this;
        }

        public UserBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UserBuilder name(String name) {
            this.name = name;
            return this;
        }

        public UserBuilder documents(List<com.insurance.manager.model.Document> documents) {
            this.documents = documents;
            return this;
        }

        public UserBuilder password(String password) {
            this.password = password;
            return this;
        }

        public UserBuilder role(String role) {
            this.role = role;
            return this;
        }

        public UserBuilder avatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
            return this;
        }

        public UserBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public User build() {
            return new User(id, email, name, documents, password, role, avatarUrl, createdAt);
        }
    }
}
