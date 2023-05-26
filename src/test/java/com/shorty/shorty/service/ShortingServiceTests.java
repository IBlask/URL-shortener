package com.shorty.shorty.service;

import com.shorty.shorty.ShortyApplication;
import com.shorty.shorty.dto.request.RequestShort;
import com.shorty.shorty.dto.response.ResponseShort;
import com.shorty.shorty.entity.User;
import com.shorty.shorty.repository.UrlRepository;
import com.shorty.shorty.repository.UserRepository;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
public class ShortingServiceTests {

    @Mock
    UrlRepository urlRepository;
    @Mock
    UserRepository userRepository;
    @InjectMocks
    ShortingService shortingService;

    @Test
    public void short_test_goodRequest() {
        RequestShort request = mock(RequestShort.class);
        request.setUrl("https://www.google.com/");
        when(request.isEmpty()).thenReturn(false);
        when(request.urlIsBlank()).thenReturn(false);

        User user = new User();
        user.setUsername("user");
        user.setPassword("pass");
        when(userRepository.findByUsername("user")).thenReturn(user);

        String authToken = Base64.getEncoder().encodeToString(("user:pass").getBytes());
        authToken = "Basic" + authToken;

        ResponseShort response = shortingService.shorting(request, urlRepository, authToken, userRepository);

        String shortUrl = response.getShortUrl();
        String shortUrlId = shortUrl.substring(shortUrl.length() - 5);
        Pattern p = Pattern.compile("[a-z]{5}");
        Matcher m = p.matcher(shortUrlId);
        boolean b = m.matches();

        assertTrue(response.getShortUrl().contains(ShortyApplication.getAddress()));
        assertTrue(b);
        assertNull(response.getDescription());
    }

    @Test
    public void short_test_badRequest() {
        RequestShort request = mock(RequestShort.class);
        when(request.isEmpty()).thenReturn(true);
        when(request.urlIsBlank()).thenReturn(false);

        User user = new User();
        user.setUsername("user");
        user.setPassword("pass");
        when(userRepository.findByUsername("user")).thenReturn(user);

        String authToken = Base64.getEncoder().encodeToString(("user:pass").getBytes());
        authToken = "Basic" + authToken;

        ResponseShort response = shortingService.shorting(request, urlRepository, authToken, userRepository);

        assertEquals("Error occurred! Please try again.", response.getDescription());
        assertNull(response.getShortUrl());
    }

    @Test
    public void short_test_blankRequest() {
        RequestShort request = mock(RequestShort.class);
        request.setUrl("");
        when(request.isEmpty()).thenReturn(false);
        when(request.urlIsBlank()).thenReturn(true);

        User user = new User();
        user.setUsername("user");
        user.setPassword("pass");
        when(userRepository.findByUsername("user")).thenReturn(user);

        String authToken = Base64.getEncoder().encodeToString(("user:pass").getBytes());
        authToken = "Basic" + authToken;

        ResponseShort response = shortingService.shorting(request, urlRepository, authToken, userRepository);

        assertEquals("Please enter your URL!", response.getDescription());
        assertNull(response.getShortUrl());
    }

    @Test
    public void short_test_missingAuthToken() {
        RequestShort request = mock(RequestShort.class);
        request.setUrl("https://www.google.com/");
        when(request.isEmpty()).thenReturn(false);
        when(request.urlIsBlank()).thenReturn(false);

        ResponseShort response = shortingService.shorting(request, urlRepository, null, userRepository);

        assertEquals("Access denied! Please log in.", response.getDescription());
        assertNull(response.getShortUrl());
    }

    @Test
    public void short_test_wrongAuthToken() {
        RequestShort request = mock(RequestShort.class);
        request.setUrl("https://www.google.com/");
        when(request.isEmpty()).thenReturn(false);
        when(request.urlIsBlank()).thenReturn(false);

        when(userRepository.findByUsername("user")).thenReturn(null);

        String authToken = Base64.getEncoder().encodeToString(("user:pass").getBytes());
        authToken = "Basic" + authToken;

        ResponseShort response = shortingService.shorting(request, urlRepository, authToken, userRepository);

        assertEquals("Access denied! Wrong username and/or password.", response.getDescription());
        assertNull(response.getShortUrl());
    }

    @Test
    public void short_test_missingDataInAuthToken() {
        RequestShort request = mock(RequestShort.class);
        request.setUrl("https://www.google.com/");
        when(request.isEmpty()).thenReturn(false);
        when(request.urlIsBlank()).thenReturn(false);

        when(userRepository.findByUsername("")).thenReturn(null);

        String authToken = Base64.getEncoder().encodeToString((":").getBytes());
        authToken = "Basic" + authToken;

        ResponseShort response = shortingService.shorting(request, urlRepository, authToken, userRepository);

        assertEquals("Please enter your username and password.", response.getDescription());
        assertNull(response.getShortUrl());
    }

    @Test
    public void short_test_missingUsernameInAuthToken() {
        RequestShort request = mock(RequestShort.class);
        request.setUrl("https://www.google.com/");
        when(request.isEmpty()).thenReturn(false);
        when(request.urlIsBlank()).thenReturn(false);

        when(userRepository.findByUsername("")).thenReturn(null);

        String authToken = Base64.getEncoder().encodeToString((":pass").getBytes());
        authToken = "Basic" + authToken;

        ResponseShort response = shortingService.shorting(request, urlRepository, authToken, userRepository);

        assertEquals("Please enter your username.", response.getDescription());
        assertNull(response.getShortUrl());
    }

    @Test
    public void short_test_missingPasswordInAuthToken() {
        RequestShort request = mock(RequestShort.class);
        request.setUrl("https://www.google.com/");
        when(request.isEmpty()).thenReturn(false);
        when(request.urlIsBlank()).thenReturn(false);

        when(userRepository.findByUsername("user")).thenReturn(null);

        String authToken = Base64.getEncoder().encodeToString(("user:").getBytes());
        authToken = "Basic" + authToken;

        ResponseShort response = shortingService.shorting(request, urlRepository, authToken, userRepository);

        assertEquals("Please enter your password.", response.getDescription());
        assertNull(response.getShortUrl());
    }

    @Test
    public void short_test_EnteredUrlIsMissingHttp() {
        RequestShort request = mock(RequestShort.class);
        request.setUrl("www.google.com/");
        when(request.isEmpty()).thenReturn(false);
        when(request.urlIsBlank()).thenReturn(false);
        when(request.EnteredUrlIsNotValid()).thenReturn("Entered URL is not valid! Please use 'http://' or 'https://'");

        User user = new User();
        user.setUsername("user");
        user.setPassword("pass");
        when(userRepository.findByUsername("user")).thenReturn(user);

        String authToken = Base64.getEncoder().encodeToString(("user:pass").getBytes());
        authToken = "Basic" + authToken;

        ResponseShort response = shortingService.shorting(request, urlRepository, authToken, userRepository);

        assertNull(response.getShortUrl());
        assertEquals("Entered URL is not valid! Please use 'http://' or 'https://'", response.getDescription());
    }

    @Test
    public void short_test_EnteredUrlIsNotValid() {
        RequestShort request = mock(RequestShort.class);
        request.setUrl("http://www.google.com/a b/");
        when(request.isEmpty()).thenReturn(false);
        when(request.urlIsBlank()).thenReturn(false);
        when(request.EnteredUrlIsNotValid()).thenReturn("Entered URL is not valid!");

        User user = new User();
        user.setUsername("user");
        user.setPassword("pass");
        when(userRepository.findByUsername("user")).thenReturn(user);

        String authToken = Base64.getEncoder().encodeToString(("user:pass").getBytes());
        authToken = "Basic" + authToken;

        ResponseShort response = shortingService.shorting(request, urlRepository, authToken, userRepository);

        assertNull(response.getShortUrl());
        assertEquals("Entered URL is not valid!", response.getDescription());
    }



    @Test
    public void generateShortUrlId_test_emptyDB() {
        String response = shortingService.generateShortUrlId(null, urlRepository).getFirst();

        assertEquals("abcde", response);
    }

    @Test
    public void generateShortUrlId_test_rowAlreadyInDB() {
        String response = shortingService.generateShortUrlId("abcde", urlRepository).getFirst();

        assertEquals("abcdf", response);
    }

    @Test
    public void generateShortUrlId_test_changingPreviousLetter() {
        String response = shortingService.generateShortUrlId("abcdz", urlRepository).getFirst();

        assertEquals("abcea", response);
    }

    @Test
    public void generateShortUrlId_test_addingNewLetter() {
        String response = shortingService.generateShortUrlId("zzzzz", urlRepository).getFirst();

        assertEquals("abcdea", response);
    }

    @Test
    public void generateShortUrlId_test_DbLimit() {
        String response = shortingService.generateShortUrlId("zzzzzzzz", urlRepository).getFirst();

        assertEquals("abcde", response);
    }
}
