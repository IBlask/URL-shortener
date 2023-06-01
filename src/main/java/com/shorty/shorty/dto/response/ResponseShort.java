package com.shorty.shorty.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.shorty.shorty.ApplicationProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ResponseShort {
    @Value("${server.domain}")
    String serverDomain;

    private String shortUrl;
    private String description = "Error occurred! Please try again.";

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrlId) {
        this.shortUrl = ApplicationProperties.getServerDomain() + shortUrlId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
