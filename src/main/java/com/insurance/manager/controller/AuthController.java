package com.insurance.manager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("/")
    public String home() {
        return "home";
    }

    private final com.insurance.manager.service.UserService userService;

    public AuthController(com.insurance.manager.service.UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @org.springframework.web.bind.annotation.PostMapping("/register")
    public String processRegistration(@org.springframework.web.bind.annotation.RequestParam String name,
            @org.springframework.web.bind.annotation.RequestParam String email,
            @org.springframework.web.bind.annotation.RequestParam String password,
            org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        try {
            userService.registerUser(name, email, password);
            redirectAttributes.addFlashAttribute("message", "Registration successful! Please login.");
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error: " + e.getMessage());
            return "redirect:/register";
        }
    }
}
