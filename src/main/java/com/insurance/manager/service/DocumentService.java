package com.insurance.manager.service;

import com.insurance.manager.model.Document;
import com.insurance.manager.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DocumentService {
    Document uploadDocument(MultipartFile file, String title, String policyNumber, LocalDate startDate,
            LocalDate endDate, User uploader);

    List<Document> getAllDocuments(User uploader);

    Page<Document> getPaginatedDocuments(User uploader, Pageable pageable);

    Optional<Document> getDocument(Long id);

    void deleteDocument(Long id);
}
