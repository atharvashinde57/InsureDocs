package com.insurance.manager.config;

import com.insurance.manager.model.User;
import com.insurance.manager.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Configuration
public class DataInitializer {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    @Bean
    public CommandLineRunner demoData(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            log.info("Initializing Demo Data...");

            // Create a default user if not exists
            String email = "admin@example.com";
            Optional<User> existingUser = userRepository.findByEmail(email);

            if (!existingUser.isPresent()) {
                User admin = User.builder()
                        .name("Admin User")
                        .email(email)
                        .password(passwordEncoder.encode("password"))
                        .role("ROLE_ADMIN")
                        .avatarUrl("https://ui-avatars.com/api/?name=Admin+User&background=ff6b00&color=fff")
                        .build();

                userRepository.save(admin);
                log.info("Created default user: {}", email);
            }
        };
    }
}
