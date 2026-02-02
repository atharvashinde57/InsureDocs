package com.insurance.manager.service;

import com.insurance.manager.model.User;

public interface UserService {
    User processOAuthPostLogin(String email, String name);

    User registerUser(String name, String email, String password);
}
