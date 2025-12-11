package com.learn.test.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthCode {
    private String email;
    private long expiresAt;
}
