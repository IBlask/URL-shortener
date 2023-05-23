package com.shorty.shorty.dto.request;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class RequestShortTests {
    @Test
    public void enteredUrlIsNotValid_test_validUrl() {
        RequestShort requestShort = new RequestShort();
        requestShort.setUrl("https://wwww.google.com/");

        assertNull(requestShort.EnteredUrlIsNotValid());
    }

    @Test
    public void enteredUrlIsNotValid_test_missingHttp() {
        RequestShort requestShort = new RequestShort();
        requestShort.setUrl("wwww.google.com/");

        assertEquals("Entered URL is not valid! Please use 'http://' or 'https://'", requestShort.EnteredUrlIsNotValid());
    }

    @Test
    public void enteredUrlIsNotValid_test_spaceInDomain() {
        RequestShort requestShort = new RequestShort();
        requestShort.setUrl("https://wwww.goog le.com/");

        assertEquals("Entered URL is not valid!", requestShort.EnteredUrlIsNotValid());
    }

    @Test
    public void enteredUrlIsNotValid_test_spaceInPath() {
        RequestShort requestShort = new RequestShort();
        requestShort.setUrl("https://wwww.google.com/ab cd/");

        assertEquals("Entered URL is not valid!", requestShort.EnteredUrlIsNotValid());
    }

    @Test
    public void enteredUrlIsNotValid_test_forbiddenCharacter1() {
        RequestShort requestShort = new RequestShort();
        requestShort.setUrl("https://wwww.goo&gle.com/");

        assertEquals("Entered URL is not valid!", requestShort.EnteredUrlIsNotValid());
    }

    @Test
    public void enteredUrlIsNotValid_test_forbiddenCharacter2() {
        RequestShort requestShort = new RequestShort();
        requestShort.setUrl("https://wwww.goo*gle.com/");

        assertEquals("Entered URL is not valid!", requestShort.EnteredUrlIsNotValid());
    }

    @Test
    public void enteredUrlIsNotValid_test_invalidDomainName() {
        RequestShort requestShort = new RequestShort();
        requestShort.setUrl("https://wwww.google.cmo/");

        assertEquals("Entered URL is not valid!", requestShort.EnteredUrlIsNotValid());
    }
}
