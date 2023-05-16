package com.shorty.shorty.service;

import com.shorty.shorty.dto.request.RequestShort;
import com.shorty.shorty.dto.response.ResponseShort;
import com.shorty.shorty.repository.UrlRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ShortingServiceTests {

    @Autowired
    ShortingService shortingService;
    @Mock
    UrlRepository urlRepository;

    @Test
    public void short_test_goodRequest() {
        RequestShort request = mock(RequestShort.class);
        request.setUrl("https://www.google.com/");
        when(request.isEmpty()).thenReturn(false);
        when(request.urlIsBlank()).thenReturn(false);

        ResponseShort response = shortingService.shorting(request, urlRepository);

        String shortUrl = response.getShortUrl();
        String shortUrlId = shortUrl.substring(shortUrl.length() - 5);
        Pattern p = Pattern.compile("[a-z]{5}");
        Matcher m = p.matcher(shortUrlId);
        boolean b = m.matches();

        assertTrue(response.getShortUrl().contains("http://localhost:8080/"));
        assertTrue(b);
        assertNull(response.getDescription());
    }

    @Test
    public void short_test_badRequest() {
        RequestShort request = mock(RequestShort.class);
        when(request.isEmpty()).thenReturn(true);
        when(request.urlIsBlank()).thenReturn(false);

        ResponseShort response = shortingService.shorting(request, urlRepository);

        assertEquals("Error occurred! Please try again.", response.getDescription());
        assertNull(response.getShortUrl());
    }

    @Test
    public void short_test_blankRequest() {
        RequestShort request = mock(RequestShort.class);
        request.setUrl("");
        when(request.isEmpty()).thenReturn(false);
        when(request.urlIsBlank()).thenReturn(true);

        ResponseShort response = shortingService.shorting(request, urlRepository);

        assertEquals("Please enter your URL!", response.getDescription());
        assertNull(response.getShortUrl());
    }

}
