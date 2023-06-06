package com.shorty.shorty.controller;

import com.shorty.shorty.dto.request.RequestRegister;
import com.shorty.shorty.dto.request.RequestShort;
import com.shorty.shorty.dto.response.ResponseRegister;
import com.shorty.shorty.dto.response.ResponseShort;
import com.shorty.shorty.repository.UrlRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class RedirectionControllerIntegrationTests {

    @Autowired
    UrlRepository urlRepository;

    @Test
    public void redirect_test_urlInDB() throws Exception {
        //ADDING USER TO DB
        TestRestTemplate restTemplate_reg = new TestRestTemplate();
        final String baseUrl_reg = "http://localhost:8080/administration/register";
        URI uri_reg = new URI(baseUrl_reg);
        RequestRegister requestRegister = new RequestRegister("user");

        HttpHeaders headers_reg = new HttpHeaders();
        headers_reg.set("X-COM-PERSIST", "true");

        HttpEntity<RequestRegister> request_reg = new HttpEntity<>(requestRegister, headers_reg);

        ResponseEntity<ResponseRegister> result_reg = restTemplate_reg.postForEntity(uri_reg, request_reg, ResponseRegister.class);
        ResponseRegister response_reg = (ResponseRegister) result_reg.getBody();

        if(result_reg.getStatusCodeValue() == 400 || !response_reg.isSuccess()) {
            fail("Error occurred while registering new user!");
        }

        //SHORTING
        TestRestTemplate restTemplate_short = new TestRestTemplate();
        final String baseUrl_short = "http://localhost:8080/administration/short";
        URI uri_short = new URI(baseUrl_short);
        RequestShort requestShort = new RequestShort("https://www.google.com/", 302);

        HttpHeaders headers_short = new HttpHeaders();
        headers_short.set("X-COM-PERSIST", "true");
        String credentials = Base64.getEncoder().encodeToString(("user:"+response_reg.getPassword()).getBytes("UTF-8"));
        headers_short.set("Authorization", "Basic" + credentials);

        HttpEntity<RequestShort> request_short = new HttpEntity<>(requestShort, headers_short);

        ResponseEntity<ResponseShort> result_short = restTemplate_short.postForEntity(uri_short, request_short, ResponseShort.class);
        ResponseShort response_short = result_short.getBody();

        String shortUrlId = response_short.getShortUrl().substring(response_short.getShortUrl().length() - 5);

        if(result_short.getStatusCodeValue() == 400 || response_short == null) {
            fail("Error occurred while shorting!");
        }

        //TESTING
        TestRestTemplate restTemplate = new TestRestTemplate();
        final String baseUrl = "http://localhost:8080/abcde";
        URI uri = new URI(baseUrl);

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Object> request = new HttpEntity<>(null, headers);

        ResponseEntity<HttpServletResponse> result = restTemplate.exchange(baseUrl, HttpMethod.GET, request, HttpServletResponse.class);
        HttpHeaders headers_redirect = result.getHeaders();

        assertEquals("302 FOUND", result.getStatusCode().toString());
        assertEquals("https://www.google.com/", headers_redirect.get("Location").get(0));
    }

    @Test
    public void redirect_test_urlNotInDB() throws Exception {
        TestRestTemplate restTemplate = new TestRestTemplate();
        final String baseUrl = "http://localhost:8080/abcdz";
        URI uri = new URI(baseUrl);

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Object> request = new HttpEntity<>(null, headers);

        ResponseEntity<HttpServletResponse> result = restTemplate.exchange(baseUrl, HttpMethod.GET, request, HttpServletResponse.class);
        HttpHeaders headers_redirect = result.getHeaders();

        assertEquals("200 OK", result.getStatusCode().toString());
        assertNull(headers_redirect.get("Location"));
        assertNull(result.getBody());
    }
}

