package com.insurance.manager.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@org.springframework.data.mongodb.core.mapping.Document(collection = "documents")
public class Document {

    @Id
    private String id;
    private String title;
    private String filename;
    private String contentType;
    private Long size;
    private String policyNumber;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    @Indexed
    private String uploaderId;
    @org.springframework.data.mongodb.core.mapping.Field("upload_date")
    private LocalDateTime uploadDate = LocalDateTime.now();

    public Document() {
    }

    public Document(String id, String title, String filename, String contentType, Long size, String policyNumber,
            LocalDate startDate, LocalDate endDate, String uploaderId, LocalDateTime uploadDate) {
        this.id = id;
        this.title = title;
        this.filename = filename;
        this.contentType = contentType;
        this.size = size;
        this.policyNumber = policyNumber;
        this.startDate = startDate;
        this.endDate = endDate;
        this.uploaderId = uploaderId;
        this.uploadDate = uploadDate != null ? uploadDate : LocalDateTime.now();
    }

    public static DocumentBuilder builder() {
        return new DocumentBuilder();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getUploaderId() {
        return uploaderId;
    }

    public void setUploaderId(String uploaderId) {
        this.uploaderId = uploaderId;
    }

    public LocalDateTime getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(LocalDateTime uploadDate) {
        this.uploadDate = uploadDate;
    }

    public static class DocumentBuilder {
        private String id;
        private String title;
        private String filename;
        private String contentType;
        private Long size;
        private String policyNumber;
        private LocalDate startDate;
        private LocalDate endDate;
        private String uploaderId;
        private LocalDateTime uploadDate;

        DocumentBuilder() {
        }

        public DocumentBuilder id(String id) {
            this.id = id;
            return this;
        }

        public DocumentBuilder title(String title) {
            this.title = title;
            return this;
        }

        public DocumentBuilder filename(String filename) {
            this.filename = filename;
            return this;
        }

        public DocumentBuilder contentType(String contentType) {
            this.contentType = contentType;
            return this;
        }

        public DocumentBuilder size(Long size) {
            this.size = size;
            return this;
        }

        public DocumentBuilder policyNumber(String policyNumber) {
            this.policyNumber = policyNumber;
            return this;
        }

        public DocumentBuilder startDate(LocalDate startDate) {
            this.startDate = startDate;
            return this;
        }

        public DocumentBuilder endDate(LocalDate endDate) {
            this.endDate = endDate;
            return this;
        }

        public DocumentBuilder uploaderId(String uploaderId) {
            this.uploaderId = uploaderId;
            return this;
        }

        public DocumentBuilder uploadDate(LocalDateTime uploadDate) {
            this.uploadDate = uploadDate;
            return this;
        }

        public Document build() {
            return new Document(id, title, filename, contentType, size, policyNumber, startDate, endDate, uploaderId,
                    uploadDate);
        }
    }
}
