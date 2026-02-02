package com.insurance.manager.controller;

import com.insurance.manager.model.Document;
import com.insurance.manager.model.User;
import com.insurance.manager.repository.UserRepository;
import com.insurance.manager.service.DocumentService;
import com.insurance.manager.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;
    private final UserRepository userRepository;
    private final StorageService storageService;

    private User getCurrentUser(Object principal) {
        if (principal == null)
            return null;

        String email = null;
        if (principal instanceof OAuth2User) {
            email = ((OAuth2User) principal).getAttribute("email");
        } else if (principal instanceof org.springframework.security.core.userdetails.UserDetails) {
            email = ((org.springframework.security.core.userdetails.UserDetails) principal).getUsername();
        } else {
            email = principal.toString();
        }

        if (email == null)
            return null;

        return userRepository.findByEmail(email).orElse(null);
    }

    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal Object principal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            Model model) {
        User user = getCurrentUser(principal);
        if (user == null) {
            return "redirect:/login";
        }

        Page<Document> documentPage = documentService.getPaginatedDocuments(user,
                PageRequest.of(page, size, Sort.by("uploadDate").descending()));

        model.addAttribute("documentPage", documentPage);
        model.addAttribute("documents", documentPage.getContent());
        model.addAttribute("user", user);

        int totalPages = documentPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "dashboard";
    }

    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam(value = "policyNumber", required = false) String policyNumber,
            @RequestParam(value = "startDate", required = false) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) LocalDate endDate,
            @AuthenticationPrincipal Object principal,
            RedirectAttributes redirectAttributes) {
        User user = getCurrentUser(principal);
        if (user == null)
            return "redirect:/login";

        try {
            documentService.uploadDocument(file, title, policyNumber, startDate, endDate, user);
            redirectAttributes.addFlashAttribute("message", "File uploaded successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Could not upload file: " + e.getMessage());
        }
        return "redirect:/dashboard";
    }

    @GetMapping("/document/{id}")
    public String viewDocumentDetails(@PathVariable String id, @AuthenticationPrincipal Object principal,
            Model model) {
        User user = getCurrentUser(principal);
        Document doc = documentService.getDocument(id).orElse(null);

        if (doc == null || !doc.getUploaderId().equals(user.getId())) {
            return "redirect:/dashboard?error=Document+not+found+or+access+denied";
        }

        model.addAttribute("document", doc);
        return "document-details";
    }

    @GetMapping("/download/{id}")
    @ResponseBody
    public ResponseEntity<Resource> download(@PathVariable String id, @AuthenticationPrincipal Object principal) {
        User user = getCurrentUser(principal);
        Document doc = documentService.getDocument(id).orElse(null);
        if (doc == null || !doc.getUploaderId().equals(user.getId())) {
            return ResponseEntity.notFound().build();
        }

        Resource file = storageService.loadAsResource(doc.getFilename());
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + doc.getFilename() + "\"").body(file);
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable String id,
            @AuthenticationPrincipal Object principal,
            RedirectAttributes redirectAttributes) {

        User user = getCurrentUser(principal);
        Document doc = documentService.getDocument(id).orElse(null);

        if (doc != null && doc.getUploaderId().equals(user.getId())) {
            documentService.deleteDocument(id);
            redirectAttributes.addFlashAttribute("message", "Document deleted.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Access denied or document not found.");
        }

        return "redirect:/dashboard";
    }
}
