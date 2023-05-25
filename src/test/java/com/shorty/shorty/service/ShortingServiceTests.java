package com.shorty.shorty.service;

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
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
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
        user.setPassword("pass");
        when(userRepository.findByUsername("user")).thenReturn(user);
        String authToken = Base64.getEncoder().encodeToString(("user:pass").getBytes());
        authToken = "Basic" + authToken;

        List<Url> listOfUrls = new ArrayList<>();
        Url url = new Url("https://www.google.com/", "abcde", 0);
        url.incrementRedirects();
        listOfUrls.add(url);
        url = new Url("https://www.google.hr/", "abcdf", 0);
        listOfUrls.add(url);
        url.incrementRedirects(); url.incrementRedirects();
        url = new Url("https://www.google.co.uk/", "abcdg", 0);
        listOfUrls.add(url);
        when(urlRepository.selectStatistics(0)).thenReturn(listOfUrls);

        Map<String, Integer> response = shortingService.getStatistics(authToken, userRepository, urlRepository);

        Map<String, Integer> map = new HashMap<>();
        map.put("https://www.google.com/", 1);
        map.put("https://www.google.hr/", 2);
        map.put("https://www.google.co.uk/", 0);

        assertEquals(map, response);
    }

    @Test
    public void statistics_test_emptyDB() {
        User user = new User();
        user.setUsername("user");
        user.setPassword("pass");
        when(userRepository.findByUsername("user")).thenReturn(user);
        String authToken = Base64.getEncoder().encodeToString(("user:pass").getBytes());
        authToken = "Basic" + authToken;

        List<Url> listOfUrls = new ArrayList<>();
        when(urlRepository.selectStatistics(0)).thenReturn(listOfUrls);

        Map<String, Integer> response = shortingService.getStatistics(authToken, userRepository, urlRepository);

        Map<String, Integer> emptyMap = new HashMap<>();

        assertEquals(emptyMap, response);
    }

    @Test
    public void statistics_test_missingAuthToken() {
        when(userRepository.findByUsername("")).thenReturn(null);

        List<Url> listOfUrls = new ArrayList<>();
        when(urlRepository.selectStatistics(0)).thenReturn(listOfUrls);

        Map<String, Integer> response = shortingService.getStatistics(null, userRepository, urlRepository);

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
        when(urlRepository.selectStatistics(0)).thenReturn(listOfUrls);

        Map<String, Integer> response = shortingService.getStatistics(authToken, userRepository, urlRepository);

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
        when(urlRepository.selectStatistics(0)).thenReturn(listOfUrls);

        Map<String, Integer> response = shortingService.getStatistics(authToken, userRepository, urlRepository);

        assertNull(response);
    }

    @Test
    public void statistics_test_userNotRegistered() {
        when(userRepository.findByUsername("user")).thenReturn(null);
        String authToken = Base64.getEncoder().encodeToString(("user:pass").getBytes());
        authToken = "Basic" + authToken;

        List<Url> listOfUrls = new ArrayList<>();
        when(urlRepository.selectStatistics(0)).thenReturn(listOfUrls);

        Map<String, Integer> response = shortingService.getStatistics(authToken, userRepository, urlRepository);

        assertNull(response);
    }
}
