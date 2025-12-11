package com.learn.test.service.impl;

import com.learn.test.dto.*;
import com.learn.test.entity.Account;
import com.learn.test.entity.User;
import com.learn.test.enumerate.LoginType;
import com.learn.test.exception.BadCredentialsException;
import com.learn.test.exception.ResourceNotFoundException;
import com.learn.test.service.IAccountService;
import com.learn.test.service.IAuthService;
import com.learn.test.service.IJwtService;
import com.learn.test.service.IUserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {
    private final TemporaryCodeService temporaryCodeService;
    private final IAccountService accountService;
    private final IJwtService jwtService;
    private final IUserService userService;

    public LoginInfo login(AuthInfo request) {
        Account account = accountService.verifyLogin(request.getEmail(), request.getPassword());
        User user = account.getUser();
        String token = jwtService.generateToken(user);
        return LoginInfo.builder()
                .name(user.getName())
                .token(token)
                .user_id(user.getId())
                .build();
    }

    @Transactional
    public String register(AuthInfo request) {
        User user = userService.createUser(request.getEmail());
        accountService.createAccount(user, request.getEmail(), request.getPassword());
        return "Register ok";
    }

    @Override
    public String forgetPassword(ForgetInfo request) {
        return accountService.forgetPassword(request.getEmail());
    }

    @Override
    public String resetPassword(ResetPasswordInfo request) {
        return accountService.resetPassword(request.getToken(),request.getNewPassword());
    }


    @Override
    public LoginInfo exchange(String code) {

        String email = temporaryCodeService.getEmail(code);
        if (email == null) {
            throw new BadCredentialsException("Invalid or expired code");
        }
        temporaryCodeService.delete(code);

        User user = userService.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String token = jwtService.generateToken(user);

        return LoginInfo.builder()
                .token(token)
                .user_id(user.getId())
                .name(user.getName())
                .build();
    }
}
