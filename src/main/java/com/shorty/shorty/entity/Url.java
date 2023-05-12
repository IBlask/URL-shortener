package com.shorty.shorty.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "URL_TABLE")
public class Url {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "url_id")
    private int url_id;
    @Column(name = "full_url")
    private String fullUrl;
    @Column(name = "short_url")
    private String shortUrl;
    @Column(name = "short_url_id")
    private String shortUrlId;
    @Column(name = "redirects")
    private int redirects = 0;
    @Column(name = "redirect_type")
    private int redirectType = 302;

    public Url() {
        super();
    }

    public Url(String fullUrl, String shortUrlId, int redirectType) {
        this.fullUrl = fullUrl;
        this.shortUrlId = shortUrlId;
        this.shortUrl = "http://localhost:8080/" + shortUrlId;
        this.setRedirectType(redirectType);
    }

    public Url(String fullUrl, String shortUrlId) {
        this(fullUrl, shortUrlId, 302);
    }

    public String getFullUrl() {
        return fullUrl;
    }

    public void setFullUrl(String fullUrl) {
        this.fullUrl = fullUrl;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrlId) {
        this.shortUrlId = shortUrlId;
        this.shortUrl = "http://localhost:8080/" + shortUrlId;
    }

    public String getShortUrlId() {
        return shortUrlId;
    }

    public void incrementRedirects() {
        this.redirects++;
    }

    public int getRedirectType() {
        return redirectType;
    }

    public void setRedirectType(int redirectType) {
        if (redirectType == 301) {
            this.redirectType = 301;
        }
        else {
            this.redirectType = 302;
        }
    }
}
