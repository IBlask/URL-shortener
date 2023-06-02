package com.shorty.shorty.dto.request;

import org.apache.commons.validator.routines.UrlValidator;

public class RequestShort {
    private String url;
    private int redirectType = 302;

    public RequestShort() {
        super();
    }

    public RequestShort(String url, int redirectType) {
        this.url = url;
        this.redirectType = redirectType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getRedirectType() {
        return redirectType;
    }

    public void setRedirectType(int redirectType) {
        this.redirectType = redirectType;
    }

    public boolean isEmpty() {
        return url == null;
    }

    public boolean urlIsBlank() {
        return url.isBlank();
    }

    public String EnteredUrlIsNotValid() {
        UrlValidator urlValidator = new UrlValidator();

        if (!urlValidator.isValid(this.url)) {
            if (!this.url.contains("http://") && !this.url.contains("https://")) {
                return "Entered URL is not valid! Please use 'http://' or 'https://'";
            }
            return "Entered URL is not valid!";
        }

        return null;
    }
}
