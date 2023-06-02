package com.shorty.shorty.dto.response;

public class ResponseStatistics {
    //TODO ResponseStatistics
    private String fullUrl;
    private String shortUrl;
    private int redirects;

    public ResponseStatistics(String fullUrl, String shortUrl, int redirects) {
        this.fullUrl = fullUrl;
        this.shortUrl = shortUrl;
        this.redirects = redirects;
    }

    public String getFullUrl() {
        return fullUrl;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public int getRedirects() {
        return redirects;
    }
}
