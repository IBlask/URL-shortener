package com.shorty.shorty.service;

import com.shorty.shorty.entity.Url;
import com.shorty.shorty.repository.UrlRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

@Service
public class RedirectionService {

    public void redirect(String shortUrlId, UrlRepository urlRepository, HttpServletResponse response) {
        Url redirectData = urlRepository.findByShortUrl(shortUrlId);

        if (redirectData == null) {
            //TODO url-not-found page
            return;
        }

        redirectData.incrementRedirects();
        urlRepository.save(redirectData);

        response.setStatus(redirectData.getRedirectType());
        response.setHeader("Location", redirectData.getFullUrl());
        response.setHeader("Connection", "close");
    }
}
