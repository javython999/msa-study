package com.errday.userservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
public class BCryptPasswordEncoderTest {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Test
    void encodePassword() {
        String plainText = "password";

        String encodedPassword = bCryptPasswordEncoder.encode(plainText);

        bCryptPasswordEncoder.matches(encodedPassword, bCryptPasswordEncoder.encode(plainText));
    }
}
