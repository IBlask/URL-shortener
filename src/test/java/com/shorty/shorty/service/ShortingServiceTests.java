package com.shorty.shorty.service;

import com.shorty.shorty.dto.request.RequestShort;
import com.shorty.shorty.dto.response.ResponseShort;
import com.shorty.shorty.repository.UrlRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ShortingServiceTests {

    @Autowired
    ShortingService shortingService;
    @Autowired
    UrlRepository urlRepository;

    @Test
    public void short_test_goodRequest() {


        RequestShort request = mock(RequestShort.class);
        request.setUrl("https://www.google.com/");
        when(request.isEmpty()).thenReturn(false);
        when(request.urlIsBlank()).thenReturn(false);

        ResponseShort responseShort = shortingService.shorting(request, urlRepository);

        assertNotNull(responseShort);
    }
}
