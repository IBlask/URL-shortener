package com.shorty.shorty.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.shorty.shorty.ShortyApplication;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ResponseShort {
    private String shortUrl;
    private String description = "Error occurred! Please try again.";

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrlId) {
        this.shortUrl = ShortyApplication.getAddress() + shortUrlId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
