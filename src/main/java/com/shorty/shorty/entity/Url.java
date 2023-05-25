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
    @Column(name = "short_url_id")
    private String shortUrlId;
    @Column(name = "redirects")
    private int redirects = 0;
    @Column(name = "redirect_type")
    private int redirectType = 302;
    @Column(name = "user_id")
    private int userId;

    public Url() {
        super();
    }

    public Url(String fullUrl, String shortUrlId, int redirectType, int userId) {
        this.fullUrl = fullUrl;
        this.shortUrlId = shortUrlId;
        this.setRedirectType(redirectType);
        this.userId = userId;
    }

    public Url(String fullUrl, String shortUrlId, int userId) {
        this(fullUrl, shortUrlId, 302, userId);
    }

    public String getFullUrl() {
        return fullUrl;
    }

    public void setFullUrl(String fullUrl) {
        this.fullUrl = fullUrl;
    }

    public String getShortUrlId() {
        return shortUrlId;
    }

    public void setShortUrlId(String shortUrlId) {
        this.shortUrlId = shortUrlId;
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

    public void setUserId(int userId) {
        this.userId = userId;
    }
}