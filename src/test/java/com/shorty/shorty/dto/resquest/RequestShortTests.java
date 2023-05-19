package com.shorty.shorty.dto.resquest;

import com.shorty.shorty.dto.request.RequestShort;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@RunWith(SpringRunner.class)
public class RequestShortTests {

    @Test
    public void EnteredUrlIsNotValid_test_missingHttp() {
        RequestShort requestShort = new RequestShort();
        requestShort.setUrl("www.google.com");
        String response = requestShort.EnteredUrlIsNotValid();

        assertEquals("Entered URL is not valid! Please use 'http://' or 'https://'", response);
    }

    @Test
    public void EnteredUrlIsNotValid_test_wrongDomainName() {
        RequestShort requestShort = new RequestShort();
        requestShort.setUrl("https://www.google.rh");
        String response = requestShort.EnteredUrlIsNotValid();

        assertEquals("Entered URL is not valid!", response);
    }

    @Test
    public void EnteredUrlIsNotValid_test_spaceInDomain() {
        RequestShort requestShort = new RequestShort();
        requestShort.setUrl("https://www.goo gle.com");
        String response = requestShort.EnteredUrlIsNotValid();

        assertEquals("Entered URL is not valid!", response);
    }

    @Test
    public void EnteredUrlIsNotValid_test_illegalCharacterInDomain() {
        RequestShort requestShort = new RequestShort();
        requestShort.setUrl("https://www.goo&gle.com");
        String response = requestShort.EnteredUrlIsNotValid();

        assertEquals("Entered URL is not valid!", response);
    }

    @Test
    public void EnteredUrlIsNotValid_test_spaceInPath() {
        RequestShort requestShort = new RequestShort();
        requestShort.setUrl("https://www.google.com/pa th/");
        String response = requestShort.EnteredUrlIsNotValid();

        assertEquals("Entered URL is not valid!", response);
    }
}
