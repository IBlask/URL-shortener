package com.shorty.shorty.controller;

import com.shorty.shorty.dto.request.RequestLogin;
import com.shorty.shorty.dto.request.RequestRegister;
import com.shorty.shorty.dto.request.RequestShort;
import com.shorty.shorty.dto.response.ResponseLogin;
import com.shorty.shorty.dto.response.ResponseRegister;
import com.shorty.shorty.dto.response.ResponseShort;
import com.shorty.shorty.dto.response.ResponseStatistics;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AdministrationControllerIntegrationTests {

    @Test
    public void register_test_goodRequest() throws Exception {
        TestRestTemplate restTemplate = new TestRestTemplate();
        final String baseUrl = "http://localhost/administration/register";
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
        final String baseUrl = "http://localhost/administration/register";
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
        final String baseUrl = "http://localhost/administration/register";
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
        final String baseUrl = "http://localhost/administration/register";
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
    public void login_test_registeredUser() throws Exception {
        //adding new user to database
        TestRestTemplate restTemplate_reg = new TestRestTemplate();
        final String baseUrl_reg = "http://localhost/administration/register";
        URI uri_reg = new URI(baseUrl_reg);
        RequestRegister requestRegister = new RequestRegister("ime log");

        HttpHeaders headers_reg = new HttpHeaders();
        headers_reg.set("X-COM-PERSIST", "true");

        HttpEntity<RequestRegister> request_reg = new HttpEntity<>(requestRegister, headers_reg);

        ResponseEntity<ResponseRegister> result_reg = restTemplate_reg.postForEntity(uri_reg, request_reg, ResponseRegister.class);
        ResponseRegister response_reg = result_reg.getBody();

        if (!response_reg.isSuccess()) {
            fail("Error with entering data into the database! Description: '" + response_reg.getDescription() + "'.");
        }

        //testing login
        TestRestTemplate restTemplate = new TestRestTemplate();
        final String baseUrl = "http://localhost/administration/login";
        URI uri = new URI(baseUrl);
        RequestLogin requestLogin = new RequestLogin("ime log", response_reg.getPassword());

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");

        HttpEntity<RequestLogin> request = new HttpEntity<>(requestLogin, headers);

        ResponseEntity<ResponseLogin> result = restTemplate.postForEntity(uri, request, ResponseLogin.class);
        ResponseLogin response = result.getBody();

        assertEquals(200, result.getStatusCodeValue());
        assertTrue(response.isSuccess());
    }

    @Test
    public void login_test_unregisteredUser() throws Exception {
        TestRestTemplate restTemplate = new TestRestTemplate();
        final String baseUrl = "http://localhost/administration/login";
        URI uri = new URI(baseUrl);
        RequestLogin requestLogin = new RequestLogin("novo ime log", "AbCdEf78");

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");

        HttpEntity<RequestLogin> request = new HttpEntity<>(requestLogin, headers);

        ResponseEntity<ResponseLogin> result = restTemplate.postForEntity(uri, request, ResponseLogin.class);
        ResponseLogin response = result.getBody();

        assertEquals(200, result.getStatusCodeValue());
        assertFalse(response.isSuccess());
    }

    @Test
    public void login_test_emptyRequest() throws Exception {
        TestRestTemplate restTemplate = new TestRestTemplate();
        final String baseUrl = "http://localhost/administration/login";
        URI uri = new URI(baseUrl);
        RequestLogin requestLogin = new RequestLogin();

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");

        HttpEntity<RequestLogin> request = new HttpEntity<>(requestLogin, headers);

        ResponseEntity<ResponseLogin> result = restTemplate.postForEntity(uri, request, ResponseLogin.class);
        ResponseLogin response = result.getBody();

        assertEquals(200, result.getStatusCodeValue());
        assertFalse(response.isSuccess());
    }

    @Test
    public void login_test_blankAccountID() throws Exception {
        TestRestTemplate restTemplate = new TestRestTemplate();
        final String baseUrl = "http://localhost/administration/login";
        URI uri = new URI(baseUrl);
        RequestLogin requestLogin = new RequestLogin("", "password");

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");

        HttpEntity<RequestLogin> request = new HttpEntity<>(requestLogin, headers);

        ResponseEntity<ResponseLogin> result = restTemplate.postForEntity(uri, request, ResponseLogin.class);
        ResponseLogin response = result.getBody();

        assertEquals(200, result.getStatusCodeValue());
        assertFalse(response.isSuccess());
    }

    @Test
    public void login_test_blankPassword() throws Exception {
        TestRestTemplate restTemplate = new TestRestTemplate();
        final String baseUrl = "http://localhost/administration/login";
        URI uri = new URI(baseUrl);
        RequestLogin requestLogin = new RequestLogin("ime2 log", "");

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");

        HttpEntity<RequestLogin> request = new HttpEntity<>(requestLogin, headers);

        ResponseEntity<ResponseLogin> result = restTemplate.postForEntity(uri, request, ResponseLogin.class);
        ResponseLogin response = result.getBody();

        assertEquals(200, result.getStatusCodeValue());
        assertFalse(response.isSuccess());
    }



    @Test
    public void statistics_test_twoUsers() throws Exception {
        //ADDING USER1 TO DB
        TestRestTemplate restTemplate_reg = new TestRestTemplate();
        final String baseUrl_reg = "http://localhost/administration/register";
        URI uri_reg = new URI(baseUrl_reg);
        RequestRegister requestRegister = new RequestRegister("statuser1");

        HttpHeaders headers_reg = new HttpHeaders();
        headers_reg.set("X-COM-PERSIST", "true");

        HttpEntity<RequestRegister> request_reg = new HttpEntity<>(requestRegister, headers_reg);

        ResponseEntity<ResponseRegister> result_reg = restTemplate_reg.postForEntity(uri_reg, request_reg, ResponseRegister.class);
        ResponseRegister response_reg = (ResponseRegister) result_reg.getBody();

        if(result_reg.getStatusCodeValue() == 400 || !response_reg.isSuccess()) {
            fail("Error occurred while registering new user!");
        }

        //ADDING USER2 TO DB
        RequestRegister requestRegister2 = new RequestRegister("statuser2");

        HttpHeaders headers_reg2 = new HttpHeaders();
        headers_reg.set("X-COM-PERSIST", "true");

        HttpEntity<RequestRegister> request_reg2 = new HttpEntity<>(requestRegister2, headers_reg2);

        ResponseEntity<ResponseRegister> result_reg2 = restTemplate_reg.postForEntity(uri_reg, request_reg2, ResponseRegister.class);
        ResponseRegister response_reg2 = (ResponseRegister) result_reg2.getBody();

        if(result_reg2.getStatusCodeValue() == 400 || !response_reg2.isSuccess()) {
            fail("Error occurred while registering new user!");
        }

        //SHORTING - USER1
        TestRestTemplate restTemplate_short = new TestRestTemplate();
        final String baseUrl_short = "http://localhost/administration/short";
        URI uri_short = new URI(baseUrl_short);
        RequestShort requestShort = new RequestShort("https://www.google.com/", 302);

        HttpHeaders headers_short = new HttpHeaders();
        headers_short.set("X-COM-PERSIST", "true");
        String credentials = Base64.getEncoder().encodeToString(("statuser1:"+response_reg.getPassword()).getBytes("UTF-8"));
        headers_short.set("Authorization", "Basic" + credentials);

        HttpEntity<RequestShort> request_short = new HttpEntity<>(requestShort, headers_short);

        ResponseEntity<ResponseShort> result_short = restTemplate_short.postForEntity(uri_short, request_short, ResponseShort.class);
        ResponseShort response_short = result_short.getBody();

        if(result_short.getStatusCodeValue() == 400 || response_short == null) {
            fail("Error occurred while shorting!");
        }


        RequestShort requestShort2 = new RequestShort("https://www.google.hr/", 302);

        HttpEntity<RequestShort> request_short2 = new HttpEntity<>(requestShort2, headers_short);

        ResponseEntity<ResponseShort> result_short2 = restTemplate_short.postForEntity(uri_short, request_short2, ResponseShort.class);
        ResponseShort response_short2 = result_short.getBody();

        if(result_short2.getStatusCodeValue() == 400 || response_short2 == null) {
            fail("Error occurred while shorting!");
        }

        //SHORTING - USER2
        RequestShort requestShort3 = new RequestShort("https://www.google.co.uk/", 302);

        HttpHeaders headers_short3 = new HttpHeaders();
        headers_short3.set("X-COM-PERSIST", "true");
        String credentials2 = Base64.getEncoder().encodeToString(("statuser2:"+response_reg2.getPassword()).getBytes("UTF-8"));
        headers_short3.set("Authorization", "Basic" + credentials2);

        HttpEntity<RequestShort> request_short3 = new HttpEntity<>(requestShort3, headers_short3);

        ResponseEntity<ResponseShort> result_short3 = restTemplate_short.postForEntity(uri_short, request_short3, ResponseShort.class);
        ResponseShort response_short3 = result_short3.getBody();

        if(result_short3.getStatusCodeValue() == 400 || response_short3 == null) {
            fail("Error occurred while shorting!");
        }

        //TESTING
        TestRestTemplate restTemplate = new TestRestTemplate();
        final String baseUrl = "http://localhost/administration/statistics";
        URI uri = new URI(baseUrl);

        HttpHeaders headers = new HttpHeaders();
        String credentials_stats = Base64.getEncoder().encodeToString(("statuser1:" + response_reg.getPassword()).getBytes("UTF-8"));
        headers.set("Authorization", "Basic" + credentials_stats);

        HttpEntity<Object> request = new HttpEntity<>(null, headers);

        ResponseEntity<List> result = restTemplate.exchange(baseUrl, HttpMethod.GET, request, List.class);
        List<ResponseStatistics> response = result.getBody();

        List<ResponseStatistics> assertedResponse = new ArrayList<>();
        assertedResponse.add(new ResponseStatistics("https://www.google.com/", "abcde", 0));
        assertedResponse.add(new ResponseStatistics("https://www.google.hr/", "abcdf", 0));

        assertEquals(200, result.getStatusCodeValue());
        for (int i = 0; i < 2; i++) {
            assert response != null;
            assertEquals(assertedResponse.get(i).getFullUrl(), response.get(i).getFullUrl());
            assertEquals(assertedResponse.get(i).getShortUrl(), response.get(i).getShortUrl());
            assertEquals(assertedResponse.get(i).getRedirects(), response.get(i).getRedirects());
        }
    }

    @Test
    public void statistics_test_emptyDB() throws Exception {
        //ADDING USER TO DB
        TestRestTemplate restTemplate_reg = new TestRestTemplate();
        final String baseUrl_reg = "http://localhost/administration/register";
        URI uri_reg = new URI(baseUrl_reg);
        RequestRegister requestRegister = new RequestRegister("empty_db_user");

        HttpHeaders headers_reg = new HttpHeaders();
        headers_reg.set("X-COM-PERSIST", "true");

        HttpEntity<RequestRegister> request_reg = new HttpEntity<>(requestRegister, headers_reg);

        ResponseEntity<ResponseRegister> result_reg = restTemplate_reg.postForEntity(uri_reg, request_reg, ResponseRegister.class);
        ResponseRegister response_reg = (ResponseRegister) result_reg.getBody();

        if(result_reg.getStatusCodeValue() == 400 || !response_reg.isSuccess()) {
            fail("Error occurred while registering new user!");
        }

        //TESTING
        TestRestTemplate restTemplate = new TestRestTemplate();
        final String baseUrl = "http://localhost/administration/statistics";
        URI uri = new URI(baseUrl);

        HttpHeaders headers = new HttpHeaders();
        String credentials_stats = Base64.getEncoder().encodeToString(("empty_db_user:" + response_reg.getPassword()).getBytes("UTF-8"));
        headers.set("Authorization", "Basic" + credentials_stats);

        HttpEntity<Object> request = new HttpEntity<>(null, headers);

        ResponseEntity<List> result = restTemplate.exchange(baseUrl, HttpMethod.GET, request, List.class);
        List<ResponseStatistics> response = result.getBody();

        List<ResponseStatistics> empty_list = new ArrayList<>();

        assertEquals(200, result.getStatusCodeValue());
        assertEquals(empty_list, response);
    }

    @Test
    public void statistics_test_missingAuthToken() throws Exception {
        TestRestTemplate restTemplate = new TestRestTemplate();
        final String baseUrl = "http://localhost/administration/statistics";
        URI uri = new URI(baseUrl);

        HttpHeaders headers = new HttpHeaders();

        HttpEntity<Object> request = new HttpEntity<>(null, headers);

        ResponseEntity<List> result = restTemplate.exchange(baseUrl, HttpMethod.GET, request, List.class);
        List<ResponseStatistics> response = result.getBody();

        assertEquals(200, result.getStatusCodeValue());
        assertNull(response);
    }

    @Test
    public void statistics_test_wrongAuthToken() throws Exception {
        TestRestTemplate restTemplate = new TestRestTemplate();
        final String baseUrl = "http://localhost/administration/statistics";
        URI uri = new URI(baseUrl);

        HttpHeaders headers = new HttpHeaders();
        String credentials_stats = Base64.getEncoder().encodeToString(("wrong_user:wrong_pass").getBytes("UTF-8"));
        headers.set("Authorization", "Basic" + credentials_stats);

        HttpEntity<Object> request = new HttpEntity<>(null, headers);

        ResponseEntity<List> result = restTemplate.exchange(baseUrl, HttpMethod.GET, request, List.class);
        List<ResponseStatistics> response = result.getBody();

        assertEquals(200, result.getStatusCodeValue());
        assertNull(response);
    }


}
