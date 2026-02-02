package com.insurance.manager.controller;

import com.insurance.manager.model.User;
import com.insurance.manager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class PoliciesController {

    private final UserRepository userRepository;
    private final com.insurance.manager.repository.DocumentRepository documentRepository;

    @GetMapping("/policies")
    public String policies(@RequestParam(required = false) String search, Model model) {
        List<User> users;
        if (search != null && !search.isEmpty()) {
            users = userRepository.findByNameContainingIgnoreCase(search);
        } else {
            users = userRepository.findAll();
        }

        // Manually populate documents for each user (since we removed @OneToMany)
        users.forEach(user -> {
            user.setDocuments(documentRepository.findByUploaderId(user.getId()));
        });

        model.addAttribute("users", users);
        model.addAttribute("search", search);
        return "policies";
    }
}
