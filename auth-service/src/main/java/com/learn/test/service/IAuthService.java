package com.learn.test.service;

import com.learn.test.dto.*;
import com.learn.test.entity.User;
import org.springframework.security.core.Authentication;

public interface IAuthService {
    LoginInfo login(AuthInfo request);
    String register(AuthInfo request);
    String forgetPassword(ForgetInfo request);
    String resetPassword(ResetPasswordInfo request);
    LoginInfo exchange(String code);
}
