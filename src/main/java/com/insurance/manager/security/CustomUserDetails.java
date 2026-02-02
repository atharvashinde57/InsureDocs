package com.insurance.manager.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class CustomUserDetails extends User {

    private final String fullName;
    private final Long id;

    public CustomUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities,
            String fullName, Long id) {
        super(username, password, authorities);
        this.fullName = fullName;
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public Long getId() {
        return id;
    }
}
