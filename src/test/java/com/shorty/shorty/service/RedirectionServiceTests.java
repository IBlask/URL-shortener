package com.shorty.shorty.service;

import com.shorty.shorty.entity.Url;
import com.shorty.shorty.repository.UrlRepository;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
public class RedirectionServiceTests {

    @Mock
    UrlRepository urlRepository;
    @Autowired
    RedirectionService redirectionService;

    @Test
    public void redirect_test_urlInDB() {
        Url mockedUrl = new Url("https://www.google.com/", "abcde", 302, 0);
        when(urlRepository.getRedirectDataByShortUrlId("abcde")).thenReturn((mockedUrl));

        MockHttpServletResponse response = new MockHttpServletResponse();

        redirectionService.redirect("abcde", urlRepository, response);

        assertEquals(302, response.getStatus());
        assertEquals("https://www.google.com/", response.getHeader("Location"));
    }

    @Test
    public void redirect_test_urlNotInDB() {
        when(urlRepository.getRedirectDataByShortUrlId("abcde")).thenReturn((null));

        MockHttpServletResponse response = new MockHttpServletResponse();

        redirectionService.redirect("abcde", urlRepository, response);

        assertEquals(200, response.getStatus());
        assertNull(response.getHeader("Location"));
    }
}
