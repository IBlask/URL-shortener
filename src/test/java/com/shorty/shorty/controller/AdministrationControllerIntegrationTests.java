package com.shorty.shorty.controller;

import com.shorty.shorty.dto.request.RequestRegister;
import com.shorty.shorty.dto.request.RequestShort;
import com.shorty.shorty.dto.response.ResponseRegister;
import com.shorty.shorty.dto.response.ResponseShort;
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
public class AdministrationControllerIntegrationTests {

    @Test
    public void register_test_goodRequest() throws Exception {
        TestRestTemplate restTemplate = new TestRestTemplate();
        final String baseUrl = "http://localhost:8080/administration/register";
        URI uri = new URI(baseUrl);
        RequestRegister requestRegister = new RequestRegister("ime reg");

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");

        HttpEntity<RequestRegister> request = new HttpEntity<>(requestRegister, headers);

        ResponseEntity<ResponseRegister> result = restTemplate.postForEntity(uri, request, ResponseRegister.class);
        ResponseRegister response = (ResponseRegister) result.getBody();

        if (response.getPassword() == null || !response.isSuccess()) {
            fail("Error occurred! Success: '" + response.isSuccess()
                    + "'. Description: '" + response.getDescription()
                    + "'. Password: '" + response.getPassword() + "'.");
        }
        Pattern p = Pattern.compile("[a-zA-Z0-9]{8}");
        Matcher m = p.matcher(response.getPassword());
        boolean b = m.matches();

        assertEquals(200, result.getStatusCodeValue());
        assertTrue(response.isSuccess());
        assertTrue(b);
    }

    @Test
    public void register_test_sameUsername() throws Exception {
        //adding user to database
        TestRestTemplate restTemplate_add = new TestRestTemplate();
        final String baseUrl = "http://localhost:8080/administration/register";
        URI uri = new URI(baseUrl);
        RequestRegister requestRegister_add = new RequestRegister("isto ime reg");

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");

        HttpEntity<RequestRegister> request_add = new HttpEntity<>(requestRegister_add, headers);

        ResponseEntity<ResponseRegister> result_add = restTemplate_add.postForEntity(uri, request_add, ResponseRegister.class);
        if (result_add.getStatusCodeValue() != 200) {
            fail("Error occurred while adding user to the database! Status code is: " + result_add.getStatusCodeValue());
        }

        ResponseRegister response_add = (ResponseRegister) result_add.getBody();

        if (!response_add.isSuccess()) {
            fail("Error occurred while adding user to the database! Success: false. Description: '" + response_add.getDescription() + "'.");
        }
        if (response_add.getPassword() == null) {
            fail("Error occurred while adding user to the database! Password is null.");
        }

        //testing with same accountID
        TestRestTemplate restTemplate = new TestRestTemplate();
        RequestRegister requestRegister = new RequestRegister("isto ime reg");

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



    @Test
    public void shorting_test_goodRequest() throws Exception {
        TestRestTemplate restTemplate = new TestRestTemplate();
        final String baseUrl = "http://localhost:8080/administration/short";
        URI uri = new URI(baseUrl);
        RequestShort requestShort = new RequestShort();
        requestShort.setUrl("https://www.google.com/");

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");

        HttpEntity<RequestShort> request = new HttpEntity<>(requestShort, headers);

        ResponseEntity<ResponseShort> result = restTemplate.postForEntity(uri, request, ResponseShort.class);
        ResponseShort response = result.getBody();

        String shortUrl = response.getShortUrl();
        String shortUrlId = shortUrl.substring(shortUrl.length() - 5);
        Pattern p = Pattern.compile("[a-z]{5}");
        Matcher m = p.matcher(shortUrlId);
        boolean b = m.matches();

        assertEquals(200, result.getStatusCodeValue());
        assertTrue(shortUrl.contains("http://localhost:8080/"));
        assertTrue(b);
    }

    @Test
    public void shorting_test_badRequest() throws Exception {
        TestRestTemplate restTemplate = new TestRestTemplate();
        final String baseUrl = "http://localhost:8080/administration/short";
        URI uri = new URI(baseUrl);
        RequestShort requestShort = new RequestShort();

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");

        HttpEntity<RequestShort> request = new HttpEntity<>(requestShort, headers);

        ResponseEntity<ResponseShort> result = restTemplate.postForEntity(uri, request, ResponseShort.class);
        ResponseShort response = result.getBody();

        assertEquals(200, result.getStatusCodeValue());
        assertEquals("Error occurred! Please try again.", response.getDescription());
        assertNull(response.getShortUrl());
    }

    @Test
    public void shorting_test_blankRequest() throws Exception {
        TestRestTemplate restTemplate = new TestRestTemplate();
        final String baseUrl = "http://localhost:8080/administration/short";
        URI uri = new URI(baseUrl);
        RequestShort requestShort = new RequestShort();
        requestShort.setUrl("");

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");

        HttpEntity<RequestShort> request = new HttpEntity<>(requestShort, headers);

        ResponseEntity<ResponseShort> result = restTemplate.postForEntity(uri, request, ResponseShort.class);
        ResponseShort response = result.getBody();

        assertEquals(200, result.getStatusCodeValue());
        assertEquals("Please enter your URL!", response.getDescription());
        assertNull(response.getShortUrl());
    }

}
