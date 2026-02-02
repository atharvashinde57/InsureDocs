package com.insurance.manager.service.impl;

import com.insurance.manager.model.User;
import com.insurance.manager.repository.UserRepository;
import com.insurance.manager.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public User processOAuthPostLogin(String email, String name) {
        return userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User newUser = User.builder()
                            .email(email)
                            .name(name)
                            .role("ROLE_USER")
                            .build();
                    return userRepository.save(newUser);
                });
    }

    @Override
    @Transactional
    public User registerUser(String name, String email, String password) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("User with email " + email + " already exists");
        }

        User newUser = User.builder()
                .name(name)
                .email(email)
                .password(passwordEncoder.encode(password))
                .role("ROLE_USER")
                .avatarUrl("https://ui-avatars.com/api/?name=" + name + "&background=ff6b00&color=fff")
                .build();

        return userRepository.save(newUser);
    }
}
