package com.shorty.shorty.service;

import com.shorty.shorty.dto.response.ResponseStatistics;
import com.shorty.shorty.entity.Url;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
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
    public void statistics_test_goodRequest() {
        User user = new User();
        user.setUsername("user");
        user.setPassword(new BCryptPasswordEncoder(10, new SecureRandom()).encode("pass"));
        when(userRepository.findByUsername("user")).thenReturn(user);
        String authToken = Base64.getEncoder().encodeToString(("user:pass").getBytes());
        authToken = "Basic" + authToken;

        List<Url> listOfUrls = new ArrayList<>();
        List<ResponseStatistics> assertedResponse = new ArrayList<>();
        Url url = new Url("https://www.google.com/", "abcde", 0);
        url.incrementRedirects();
        listOfUrls.add(url);
        assertedResponse.add(new ResponseStatistics(url.getFullUrl(), url.getShortUrl(), url.getRedirects()));
        url = new Url("https://www.google.hr/", "abcdf", 0);
        listOfUrls.add(url);
        url.incrementRedirects(); url.incrementRedirects();
        assertedResponse.add(new ResponseStatistics(url.getFullUrl(), url.getShortUrl(), url.getRedirects()));
        url = new Url("https://www.google.co.uk/", "abcdg", 0);
        listOfUrls.add(url);
        assertedResponse.add(new ResponseStatistics(url.getFullUrl(), url.getShortUrl(), url.getRedirects()));
        when(urlRepository.findAllByUserId(0)).thenReturn(listOfUrls);

        List<ResponseStatistics> response = shortingService.getStatistics(authToken, userRepository, urlRepository);

        for (int i = 0; i < 3; i++) {
            assertEquals(assertedResponse.get(i).getFullUrl(), response.get(i).getFullUrl());
            assertEquals(assertedResponse.get(i).getShortUrl(), response.get(i).getShortUrl());
            assertEquals(assertedResponse.get(i).getRedirects(), response.get(i).getRedirects());
        }
    }

    @Test
    public void statistics_test_emptyDB() {
        User user = new User();
        user.setUsername("user");
        user.setPassword(new BCryptPasswordEncoder(10, new SecureRandom()).encode("pass"));
        when(userRepository.findByUsername("user")).thenReturn(user);
        String authToken = Base64.getEncoder().encodeToString(("user:pass").getBytes());
        authToken = "Basic" + authToken;

        List<Url> listOfUrls = new ArrayList<>();
        when(urlRepository.findAllByUserId(0)).thenReturn(listOfUrls);

        List<ResponseStatistics> response = shortingService.getStatistics(authToken, userRepository, urlRepository);

        List<ResponseStatistics> emptyList = new ArrayList<>();

        assertEquals(emptyList, response);
    }

    @Test
    public void statistics_test_missingAuthToken() {
        when(userRepository.findByUsername("")).thenReturn(null);

        List<Url> listOfUrls = new ArrayList<>();
        when(urlRepository.findAllByUserId(0)).thenReturn(listOfUrls);

        List<ResponseStatistics> response = shortingService.getStatistics(null, userRepository, urlRepository);

        assertNull(response);
    }

    @Test
    public void statistics_test_missingUsernameInAuthToken() {
        User user = new User();
        user.setUsername("user");
        user.setPassword("pass");
        when(userRepository.findByUsername("user")).thenReturn(user);
        String authToken = Base64.getEncoder().encodeToString((":pass").getBytes());
        authToken = "Basic" + authToken;

        List<Url> listOfUrls = new ArrayList<>();
        when(urlRepository.findAllByUserId(0)).thenReturn(listOfUrls);

        List<ResponseStatistics> response = shortingService.getStatistics(authToken, userRepository, urlRepository);

        assertNull(response);
    }

    @Test
    public void statistics_test_missingPasswordInAuthToken() {
        User user = new User();
        user.setUsername("user");
        user.setPassword("pass");
        when(userRepository.findByUsername("user")).thenReturn(user);
        String authToken = Base64.getEncoder().encodeToString(("user:").getBytes());
        authToken = "Basic" + authToken;

        List<Url> listOfUrls = new ArrayList<>();
        when(urlRepository.findAllByUserId(0)).thenReturn(listOfUrls);

        List<ResponseStatistics> response = shortingService.getStatistics(authToken, userRepository, urlRepository);

        assertNull(response);
    }

    @Test
    public void statistics_test_userNotRegistered() {
        when(userRepository.findByUsername("user")).thenReturn(null);
        String authToken = Base64.getEncoder().encodeToString(("user:pass").getBytes());
        authToken = "Basic" + authToken;

        List<Url> listOfUrls = new ArrayList<>();
        when(urlRepository.findAllByUserId(0)).thenReturn(listOfUrls);

        List<ResponseStatistics> response = shortingService.getStatistics(authToken, userRepository, urlRepository);

        assertNull(response);
    }
}
