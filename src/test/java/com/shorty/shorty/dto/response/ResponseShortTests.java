package com.shorty.shorty.dto.response;

import com.shorty.shorty.repository.UrlRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ResponseShortTests {

    @Mock
    UrlRepository urlRepository;

    @Test
    public void generateShortUrlId_test_emptyDB() {
        ResponseShort responseShort = new ResponseShort();
        String response = responseShort.generateShortUrlId(null, urlRepository);

        assertEquals("abcde", response);
    }

    @Test
    public void generateShortUrlId_test_rowAlreadyInDB() {
        ResponseShort responseShort = new ResponseShort();
        String response = responseShort.generateShortUrlId("abcde", urlRepository);

        assertEquals("abcdf", response);
    }

    @Test
    public void generateShortUrlId_test_changingPreviousLetter() {
        ResponseShort responseShort = new ResponseShort();
        String response = responseShort.generateShortUrlId("abcdz", urlRepository);

        assertEquals("abcea", response);
    }

    @Test
    public void generateShortUrlId_test_addingNewLetter() {
        ResponseShort responseShort = new ResponseShort();
        String response = responseShort.generateShortUrlId("zzzzz", urlRepository);

        assertEquals("abcdea", response);
    }

    @Test
    public void generateShortUrlId_test_DbLimit() {
        ResponseShort responseShort = new ResponseShort();
        String response = responseShort.generateShortUrlId("zzzzzzzz", urlRepository);

        assertEquals("abcde", response);
    }
}
