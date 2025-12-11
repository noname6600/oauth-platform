package com.learn.test.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.test.dto.ApiResponse;
import com.learn.test.entity.User;
import com.learn.test.enumerate.LoginType;
import com.learn.test.service.IUserService;
import com.learn.test.service.impl.TemporaryCodeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final IUserService userService;
    private final TemporaryCodeService temporaryCodeService;

    @Value("${app.frontend.reset-password-url}")
    private String frontendBaseUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        CustomOAuth2User oauth = (CustomOAuth2User) authentication.getPrincipal();

        String email = oauth.getEmail();
        String name = oauth.getName();

        userService.findOrCreateOAuthUser(email, name);

        String code = temporaryCodeService.createCode(email);

        String redirectParam = request.getParameter("redirect_uri");

        String baseRedirect = (redirectParam == null || redirectParam.isEmpty())
                ? frontendBaseUrl + "/oauth/callback"
                : redirectParam;

        String targetUrl = baseRedirect + "?code=" + code;

        response.sendRedirect(targetUrl);
    }
}
