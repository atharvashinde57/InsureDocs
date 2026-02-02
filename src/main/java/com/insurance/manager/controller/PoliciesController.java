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

    @GetMapping("/policies")
    public String policies(@RequestParam(required = false) String search, Model model) {
        List<User> users;
        if (search != null && !search.isEmpty()) {
            users = userRepository.findByNameContainingIgnoreCase(search);
        } else {
            users = userRepository.findAll();
        }

        // Filter out users who have no documents (optional, but keeps the "Policies"
        // list clean)
        // users = users.stream().filter(u -> u.getDocuments() != null &&
        // !u.getDocuments().isEmpty()).collect(Collectors.toList());
        // For now, I'll list all users so we can see "No policies" if applicable, or
        // the admin can see everyone.

        model.addAttribute("users", users);
        model.addAttribute("search", search);
        return "policies";
    }
}
