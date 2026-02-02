package com.insurance.manager.repository;

import com.insurance.manager.model.Document;
import com.insurance.manager.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findByUploader(User uploader);

    // Pagination support
    Page<Document> findByUploader(User uploader, Pageable pageable);
}
