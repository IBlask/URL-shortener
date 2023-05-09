package com.shorty.shorty.controller;

import com.shorty.shorty.dto.request.RequestLogin;
import com.shorty.shorty.dto.request.RequestRegister;
import com.shorty.shorty.dto.response.ResponseLogin;
import com.shorty.shorty.dto.response.ResponseRegister;
import org.junit.Test;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AdministrationControllerTests {

    @Test
    @Order(1)
    public void register_test_goodRequest() throws Exception {
        TestRestTemplate restTemplate = new TestRestTemplate();
        final String baseUrl = "http://localhost:8080/administration/register";
        URI uri = new URI(baseUrl);
        RequestRegister requestRegister = new RequestRegister("ime");

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");

        HttpEntity<RequestRegister> request = new HttpEntity<>(requestRegister, headers);

        ResponseEntity<ResponseRegister> result = restTemplate.postForEntity(uri, request, ResponseRegister.class);
        ResponseRegister response = (ResponseRegister) result.getBody();

        if (response.getPassword() == null) {
            fail("Error occurred! Password is null.");
        }
        Pattern p = Pattern.compile("[a-zA-Z0-9]{8}");
        Matcher m = p.matcher(response.getPassword());
        boolean b = m.matches();

        assertEquals(200, result.getStatusCodeValue());
        assertTrue(response.isSuccess());
        //assertNull(response.getDescription());
        assertTrue(b);
    }

    @Test
    @Order(2)
    public void register_test_sameUsername() throws Exception {
        TestRestTemplate restTemplate = new TestRestTemplate();
        final String baseUrl = "http://localhost:8080/administration/register";
        URI uri = new URI(baseUrl);
        RequestRegister requestRegister = new RequestRegister("ime");

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");

        HttpEntity<RequestRegister> request = new HttpEntity<>(requestRegister, headers);

        ResponseEntity<ResponseRegister> result = restTemplate.postForEntity(uri, request, ResponseRegister.class);
        ResponseRegister response = (ResponseRegister) result.getBody();

        assertEquals(200, result.getStatusCodeValue());
        assertFalse(response.isSuccess());
        assertEquals("Account ID already exists!", response.getDescription());
        assertNull(response.getPassword());
    }

    @Test
    @Order(3)
    public void register_test_badRequest() throws Exception {
        TestRestTemplate restTemplate = new TestRestTemplate();
        final String baseUrl = "http://localhost:8080/administration/register";
        URI uri = new URI(baseUrl);
        RequestRegister requestRegister = new RequestRegister();

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");

        HttpEntity<RequestRegister> request = new HttpEntity<>(requestRegister, headers);

        ResponseEntity<ResponseRegister> result = restTemplate.postForEntity(uri, request, ResponseRegister.class);
        ResponseRegister response = (ResponseRegister) result.getBody();

        assertEquals(200, result.getStatusCodeValue());
        assertFalse(response.isSuccess());
        assertEquals("Error occurred! Please try again.", response.getDescription());
        assertNull(response.getPassword());
    }

    @Test
    @Order(4)
    public void register_test_blankRequest() throws Exception {
        TestRestTemplate restTemplate = new TestRestTemplate();
        final String baseUrl = "http://localhost:8080/administration/register";
        URI uri = new URI(baseUrl);
        RequestRegister requestRegister = new RequestRegister("");

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");

        HttpEntity<RequestRegister> request = new HttpEntity<>(requestRegister, headers);

        ResponseEntity<ResponseRegister> result = restTemplate.postForEntity(uri, request, ResponseRegister.class);
        ResponseRegister response = (ResponseRegister) result.getBody();

        assertEquals(200, result.getStatusCodeValue());
        assertFalse(response.isSuccess());
        assertEquals("Please enter your username!", response.getDescription());
        assertNull(response.getPassword());
    }



    @Test
    @Order(5)
    public void login_test_registeredUser() throws Exception {
        //adding new user to database
        TestRestTemplate restTemplate_reg = new TestRestTemplate();
        final String baseUrl_reg = "http://localhost:8080/administration/register";
        URI uri_reg = new URI(baseUrl_reg);
        RequestRegister requestRegister = new RequestRegister("ime2");

        HttpHeaders headers_reg = new HttpHeaders();
        headers_reg.set("X-COM-PERSIST", "true");

        HttpEntity<RequestRegister> request_reg = new HttpEntity<>(requestRegister, headers_reg);

        ResponseEntity<ResponseRegister> result_reg = restTemplate_reg.postForEntity(uri_reg, request_reg, ResponseRegister.class);
        ResponseRegister response_reg = result_reg.getBody();

        if (!response_reg.isSuccess()) {
            fail("Error with entering data into the database!");
        }

        //testing login
        TestRestTemplate restTemplate = new TestRestTemplate();
        final String baseUrl = "http://localhost:8080/administration/login";
        URI uri = new URI(baseUrl);
        RequestLogin requestLogin = new RequestLogin("ime2", response_reg.getPassword());

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");

        HttpEntity<RequestLogin> request = new HttpEntity<>(requestLogin, headers);

        ResponseEntity<ResponseLogin> result = restTemplate.postForEntity(uri, request, ResponseLogin.class);
        ResponseLogin response = result.getBody();

        assertEquals(200, result.getStatusCodeValue());
        assertTrue(response.isSuccess());
    }

    /*
    @Test
    @Order(6)
    public void login_test_sameUsername() throws Exception {
        TestRestTemplate restTemplate = new TestRestTemplate();
        final String baseUrl = "http://localhost:8080/administration/register";
        URI uri = new URI(baseUrl);
        RequestRegister requestRegister = new RequestRegister("ime");

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");

        HttpEntity<RequestRegister> request = new HttpEntity<>(requestRegister, headers);

        ResponseEntity<ResponseRegister> result = restTemplate.postForEntity(uri, request, ResponseRegister.class);
        ResponseRegister response = (ResponseRegister) result.getBody();

        assertEquals(200, result.getStatusCodeValue());
        assertFalse(response.isSuccess());
        assertEquals("Account ID already exists!", response.getDescription());
        assertNull(response.getPassword());
    }
    */
}
