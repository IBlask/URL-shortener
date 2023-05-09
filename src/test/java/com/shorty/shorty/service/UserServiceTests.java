package com.shorty.shorty.service;

import com.shorty.shorty.dto.request.RequestLogin;
import com.shorty.shorty.dto.request.RequestRegister;
import com.shorty.shorty.dto.response.ResponseLogin;
import com.shorty.shorty.dto.response.ResponseRegister;
import com.shorty.shorty.repository.UserRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserServiceTests {

    @Autowired
    UserService userService;
    @Mock
    UserRepository userRepository;

    @Test
    @Order(1)
    void register_test_goodRequest() {
        RequestRegister request = mock(RequestRegister.class);
        request.setAccountID("ime");
        when(request.isEmpty()).thenReturn(false);
        when(request.accountIdIsBlank()).thenReturn(false);
        when(request.accountIdExists(userRepository)).thenReturn(false);

        ResponseRegister response = userService.register(request, userRepository);

        Pattern p = Pattern.compile("[a-zA-Z0-9]{8}");
        Matcher m = p.matcher(response.getPassword());
        boolean b = m.matches();

        assertTrue(response.isSuccess());
        assertNull(response.getDescription());
        assertTrue(b);
    }

    @Test
    @Order(2)
    void register_test_sameUsername() {
        RequestRegister request = mock(RequestRegister.class);
        request.setAccountID("ime");
        when(request.isEmpty()).thenReturn(false);
        when(request.accountIdIsBlank()).thenReturn(false);
        when(request.accountIdExists(userRepository)).thenReturn(true);

        ResponseRegister response = userService.register(request, userRepository);

        assertFalse(response.isSuccess());
        assertEquals("Account ID already exists!", response.getDescription());
        assertNull(response.getPassword());
    }

    @Test
    @Order(3)
    void register_test_badRequest() {
        RequestRegister request = mock(RequestRegister.class);
        when(request.isEmpty()).thenReturn(true);

        ResponseRegister response = userService.register(request, userRepository);

        assertFalse(response.isSuccess());
        assertEquals("Error occurred! Please try again.", response.getDescription());
        assertNull(response.getPassword());
    }

    @Test
    @Order(4)
    void register_test_blankRequest() {
        RequestRegister request = mock(RequestRegister.class);
        request.setAccountID("");
        when(request.isEmpty()).thenReturn(false);
        when(request.accountIdIsBlank()).thenReturn(true);

        ResponseRegister response = userService.register(request, userRepository);

        assertFalse(response.isSuccess());
        assertEquals("Please enter your username!", response.getDescription());
        assertNull(response.getPassword());
    }



    @Test
    @Order(5)
    void login_test_registeredUser() {
        RequestLogin request = mock(RequestLogin.class);
        request.setAccountID("ime");
        request.setPassword("pass");
        when(request.isEmpty()).thenReturn(false);
        when(request.accountIdIsBlank()).thenReturn(false);
        when(request.passwordIsBlank()).thenReturn(false);
        when(request.accountIdAndPasswordAreMatching(userRepository)).thenReturn(true);

        ResponseLogin response = userService.login(request, userRepository);

        assertTrue(response.isSuccess());
    }

    @Test
    @Order(6)
    void login_test_unregisteredUser() {
        RequestLogin request = mock(RequestLogin.class);
        request.setAccountID("ime");
        request.setPassword("pass");
        when(request.isEmpty()).thenReturn(false);
        when(request.accountIdIsBlank()).thenReturn(false);
        when(request.passwordIsBlank()).thenReturn(false);
        when(request.accountIdAndPasswordAreMatching(userRepository)).thenReturn(false);

        ResponseLogin response = userService.login(request, userRepository);

        assertFalse(response.isSuccess());
    }

    @Test
    @Order(7)
    void login_test_badRequest() {
        RequestLogin request = mock(RequestLogin.class);
        when(request.isEmpty()).thenReturn(true);
        when(request.accountIdIsBlank()).thenReturn(true);
        when(request.passwordIsBlank()).thenReturn(true);
        when(request.accountIdAndPasswordAreMatching(userRepository)).thenReturn(false);

        ResponseLogin response = userService.login(request, userRepository);

        assertFalse(response.isSuccess());
    }

    @Test
    @Order(8)
    void login_test_blankAccountId() {
        RequestLogin request = mock(RequestLogin.class);
        request.setAccountID("");
        request.setPassword("pass");
        when(request.isEmpty()).thenReturn(false);
        when(request.accountIdIsBlank()).thenReturn(true);
        when(request.passwordIsBlank()).thenReturn(false);
        when(request.accountIdAndPasswordAreMatching(userRepository)).thenReturn(false);

        ResponseLogin response = userService.login(request, userRepository);

        assertFalse(response.isSuccess());
    }

    @Test
    @Order(9)
    void login_test_blankPassword() {
        RequestLogin request = mock(RequestLogin.class);
        request.setAccountID("ime");
        request.setPassword("");
        when(request.isEmpty()).thenReturn(false);
        when(request.accountIdIsBlank()).thenReturn(false);
        when(request.passwordIsBlank()).thenReturn(true);
        when(request.accountIdAndPasswordAreMatching(userRepository)).thenReturn(false);

        ResponseLogin response = userService.login(request, userRepository);

        assertFalse(response.isSuccess());
    }
}