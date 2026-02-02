package com.insurance.manager.service.impl;

import com.insurance.manager.model.Document;
import com.insurance.manager.model.User;
import com.insurance.manager.repository.DocumentRepository;
import com.insurance.manager.service.DocumentService;
import com.insurance.manager.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;
    private final StorageService storageService;

    @Override
    @Transactional
    public Document uploadDocument(MultipartFile file, String title, String policyNumber, LocalDate startDate,
            LocalDate endDate, User uploader) {
        // Validation
        if (file.getContentType() == null || !file.getContentType().equalsIgnoreCase("application/pdf")) {
            throw new IllegalArgumentException("Only PDF files are allowed.");
        }

        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.lastIndexOf(".") > 0) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        String storedFilename = UUID.randomUUID().toString() + extension;
        storageService.store(file, storedFilename);

        Document doc = Document.builder()
                .title(title)
                .policyNumber(policyNumber)
                .startDate(startDate)
                .endDate(endDate)
                .filename(storedFilename)
                .contentType(file.getContentType())
                .size(file.getSize())
                .uploader(uploader)
                .build();

        return documentRepository.save(doc);
    }

    @Override
    public List<Document> getAllDocuments(User uploader) {
        return documentRepository.findByUploader(uploader);
    }

    @Override
    public Page<Document> getPaginatedDocuments(User uploader, Pageable pageable) {
        return documentRepository.findByUploader(uploader, pageable);
    }

    @Override
    public Optional<Document> getDocument(Long id) {
        return documentRepository.findById(id);
    }

    @Override
    @Transactional
    public void deleteDocument(Long id) {
        Optional<Document> doc = documentRepository.findById(id);
        if (doc.isPresent()) {
            storageService.delete(doc.get().getFilename());
            documentRepository.delete(doc.get());
        }
    }
}
