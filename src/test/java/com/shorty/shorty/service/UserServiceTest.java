package com.shorty.shorty.service;

import com.shorty.shorty.dto.request.RequestRegister;
import com.shorty.shorty.dto.response.ResponseRegister;
import com.shorty.shorty.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class UserServiceTest {

    @Autowired UserService userService;
    @Autowired UserRepository userRepository;

    @Test
    void register_test_goodRequest() {
        RequestRegister request = new RequestRegister("ime");
        ResponseRegister response = userService.register(request, userRepository);

        Pattern p = Pattern.compile("[a-zA-Z0-9]{8}");
        Matcher m = p.matcher(response.getPassword());
        boolean b = m.matches();

        assertTrue(response.isSuccess());
        assertNull(response.getDescription());
        assertTrue(b);
    }

    @Test
    void register_test_sameUsername() {
        RequestRegister request = new RequestRegister("ime");
        ResponseRegister response = userService.register(request, userRepository);

        assertFalse(response.isSuccess());
        assertEquals("Account ID already exists!", response.getDescription());
        assertNull(response.getPassword());
    }

    @Test
    void register_test_badRequest() {
        RequestRegister request = new RequestRegister();
        ResponseRegister response = userService.register(request, userRepository);

        assertFalse(response.isSuccess());
        assertEquals("Error occurred! Please try again.", response.getDescription());
        assertNull(response.getPassword());
    }

    @Test
    void register_test_blankRequest() {
        RequestRegister request = new RequestRegister("");
        ResponseRegister response = userService.register(request, userRepository);

        assertFalse(response.isSuccess());
        assertEquals("Please enter your username!", response.getDescription());
        assertNull(response.getPassword());
    }
}