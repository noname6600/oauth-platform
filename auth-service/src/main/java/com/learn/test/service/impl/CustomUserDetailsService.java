package com.learn.test.service.impl;

import com.learn.test.configuration.CustomUserDetails;
import com.learn.test.entity.Account;
import com.learn.test.entity.User;
import com.learn.test.repository.AccountRepository;
import com.learn.test.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User '" + email + "' not found"));

        return new CustomUserDetails(user);
    }
}

