package com.insurance.manager.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class CustomUserDetails extends User {

    private final String fullName;
    private final String id;

    public CustomUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities,
            String fullName, String id) {
        super(username, password, authorities);
        this.fullName = fullName;
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getId() {
        return id;
    }
}
