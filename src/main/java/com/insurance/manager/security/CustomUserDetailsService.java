package com.insurance.manager.security;

import com.insurance.manager.model.User;
import com.insurance.manager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

        private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(CustomUserDetailsService.class);

        private final UserRepository userRepository;

        @Override
        public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
                log.info("Attempting to load user by email: {}", email);
                User user = userRepository.findByEmail(email)
                                .orElseThrow(() -> {
                                        log.error("User not found: {}", email);
                                        return new UsernameNotFoundException("User not found with email: " + email);
                                });

                log.info("User found: {}, Role: {}", user.getEmail(), user.getRole());
                return new CustomUserDetails(
                                user.getEmail(),
                                user.getPassword() != null ? user.getPassword() : "", // Handle OAuth users without
                                                                                      // password
                                Collections.singletonList(
                                                new SimpleGrantedAuthority(user.getRole() != null ? user.getRole()
                                                                : "ROLE_USER")),
                                user.getName(), // Assuming 'name' field exists in User entity
                                user.getId());
        }
}
