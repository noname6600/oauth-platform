package com.learn.test.service;

public interface ITemporaryCodeService {
    String createCode(String email);
    String getEmail(String code);
    void delete(String code);
}
