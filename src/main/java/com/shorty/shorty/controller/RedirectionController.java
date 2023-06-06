package com.shorty.shorty.controller;

import com.shorty.shorty.repository.UrlRepository;
import com.shorty.shorty.service.RedirectionService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedirectionController {

    @Autowired
    RedirectionService redirectionService;
    @Autowired
    UrlRepository urlRepository;

    @GetMapping("/{shortUrlId}")
    public void redirect(@PathVariable String shortUrlId, HttpServletResponse response) {
        redirectionService.redirect(shortUrlId, urlRepository, response);
    }
}
