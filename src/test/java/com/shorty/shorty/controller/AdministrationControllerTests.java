package com.shorty.shorty.controller;

import com.shorty.shorty.dto.request.RequestRegister;
import com.shorty.shorty.dto.response.ResponseRegister;
import org.junit.Test;
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
public class AdministrationControllerTests {

    @Test
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

        Pattern p = Pattern.compile("[a-zA-Z0-9]{8}");
        Matcher m = p.matcher(response.getPassword());
        boolean b = m.matches();

        assertEquals(200, result.getStatusCodeValue());
        assertTrue(response.isSuccess());
        //assertNull(response.getDescription());
        assertTrue(b);
    }

    @Test
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

}
