package com.learn.test.service.impl;

import com.learn.test.dto.UserInfo;
import com.learn.test.entity.User;
import com.learn.test.enumerate.LoginType;
import com.learn.test.exception.ConflictException;
import com.learn.test.exception.ResourceNotFoundException;
import com.learn.test.repository.UserRepository;
import com.learn.test.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;

    @Override
    public User createUser(String name) {
        userRepository.findByEmail(name).ifPresent(u -> {
            throw new ConflictException("This email already exists");
        });
        User user = User.builder()
                .name(name)
                .email(name)
                .loginType(LoginType.LOCAL)
                .build();
        return userRepository.save(user);
    }

    @Override
    public UserInfo updateMe(User user, UserInfo request) {
        user.setName(request.getName());
        user.setAddress(request.getAddress());
        user.setPhoneNumber(request.getPhoneNumber());
        userRepository.save(user);
        return UserInfo.builder()
                .address(user.getAddress())
                .name(user.getName())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }

    @Override
    public UserInfo getUserInfo(User user) {
        return UserInfo.builder()
                .name(user.getName())
                .address(user.getAddress())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User findOrCreateOAuthUser(String email, String name) {
        String googleEmail = "google:" + email.trim().toLowerCase();

        return userRepository.findByEmail(googleEmail)
                .orElseGet(() -> userRepository.save(
                        User.builder()
                                .email(googleEmail)
                                .name(name != null ? name.trim() : email.trim())
                                .loginType(LoginType.GOOGLE)
                                .build()
                ));
    }

}

