package com.insurance.manager.repository;

import com.insurance.manager.model.Document;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DocumentRepository extends MongoRepository<Document, String> {
    List<Document> findByUploaderId(String uploaderId);

    Page<Document> findByUploaderId(String uploaderId, Pageable pageable);
}
