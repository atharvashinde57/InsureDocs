package com.insurance.manager.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
public class SecurityConfig {

        private final CustomAuthenticationSuccessHandler successHandler;
        private final org.springframework.security.core.userdetails.UserDetailsService userDetailsService;
        private final org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

        @Autowired
        public SecurityConfig(CustomAuthenticationSuccessHandler successHandler,
                        org.springframework.security.core.userdetails.UserDetailsService userDetailsService,
                        org.springframework.security.crypto.password.PasswordEncoder passwordEncoder) {
                this.successHandler = successHandler;
                this.userDetailsService = userDetailsService;
                this.passwordEncoder = passwordEncoder;
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/", "/login", "/register", "/contact", "/error",
                                                                "/css/**",
                                                                "/js/**",
                                                                "/images/**")
                                                .permitAll()
                                                .anyRequest().authenticated())
                                .oauth2Login(oauth2 -> oauth2
                                                .loginPage("/login")
                                                .successHandler(successHandler))
                                .formLogin(form -> form
                                                .loginPage("/login")
                                                .usernameParameter("email") // We use email as username
                                                .defaultSuccessUrl("/dashboard", true)
                                                .permitAll())
                                .logout(logout -> logout
                                                .logoutSuccessUrl("/")
                                                .permitAll());

                // CSRF is enabled by default in Spring Security 6
                return http.build();
        }

        @Bean
        public org.springframework.security.authentication.AuthenticationProvider authenticationProvider() {
                org.springframework.security.authentication.dao.DaoAuthenticationProvider provider = new org.springframework.security.authentication.dao.DaoAuthenticationProvider();
                provider.setUserDetailsService(userDetailsService);
                provider.setPasswordEncoder(passwordEncoder);
                return provider;
        }
}
