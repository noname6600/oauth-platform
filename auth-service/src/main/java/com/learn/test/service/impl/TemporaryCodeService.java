package com.learn.test.service.impl;

import com.learn.test.dto.AuthCode;
import com.learn.test.service.ITemporaryCodeService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TemporaryCodeService implements ITemporaryCodeService {

    private final Map<String, AuthCode> store = new ConcurrentHashMap<>();

    public String createCode(String email) {
        String code = UUID.randomUUID().toString();
        long expiresAt = System.currentTimeMillis() + 60_000;

        store.put(code, new AuthCode(email, expiresAt));
        return code;
    }

    public String getEmail(String code) {
        AuthCode authCode = store.get(code);

        if (authCode == null) return null;
        if (authCode.getExpiresAt() < System.currentTimeMillis()) {
            store.remove(code);
            return null;
        }

        return authCode.getEmail();
    }

    public void delete(String code) {
        store.remove(code);
    }
}


