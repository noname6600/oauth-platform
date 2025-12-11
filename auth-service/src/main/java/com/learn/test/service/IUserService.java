package com.learn.test.service;

import com.learn.test.dto.UserInfo;
import com.learn.test.entity.User;

import java.util.Optional;

public interface IUserService {
    UserInfo getUserInfo(User user);
    User createUser(String name);
    UserInfo updateMe(User user, UserInfo request);
    Optional<User> findByEmail(String email);
    User findOrCreateOAuthUser(String email, String name);
}
